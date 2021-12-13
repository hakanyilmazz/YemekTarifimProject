package com.tezodevi.android.view.user.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tezodevi.android.R;
import com.tezodevi.android.adapter.MessageAdapter;
import com.tezodevi.android.databinding.FragmentMessageBinding;
import com.tezodevi.android.model.MessageAPI;
import com.tezodevi.android.model.MessageModel;
import com.tezodevi.android.model.ResponseUser;
import com.tezodevi.android.service.web_services.SpamAPI;
import com.tezodevi.android.service.worker_services.LoginService;
import com.tezodevi.android.service.worker_services.UserService;
import com.tezodevi.android.util.LocalhostHelper;
import com.tezodevi.android.util.SocketTagsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageFragment extends Fragment {

    private final List<MessageModel> messageList = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentMessageBinding binding;
    private LoginService loginService;
    private Socket mSocket;
    private UserService userService;
    private String photo = "";
    private Retrofit retrofit;
    private SpamAPI spamAPI;

    {
        try {
            mSocket = IO.socket(LocalhostHelper.BASE_WEBSOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginService = new LoginService(requireContext());
        userService = new UserService(requireContext());

        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(LocalhostHelper.BASE_MACHINE_LEARNING_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        spamAPI = retrofit.create(SpamAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        compositeDisposable.clear();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compositeDisposable.add(
                userService.getMe().subscribe(this::handleResponse, Throwable::printStackTrace)
        );
    }

    private void handleResponse(ResponseUser responseUser) {
        photo = responseUser.profilePhoto;

        mSocket.connect();
        mSocket.emit(SocketTagsHelper.CONNECTED_USER);

        setSocketOnListeners();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.fragmentMessageMessageEditText.getText().toString().trim();

                if (!message.isEmpty()) {

                    compositeDisposable.add(
                            spamAPI.isSpam(new MessageAPI(message)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(isSpamResult -> {
                                if (!isSpamResult.isSpam) {
                                    binding.fragmentMessageMessageEditText.setText("");
                                    mSocket.emit(SocketTagsHelper.SEND_MESSAGE, loginService.getUsername(), message, photo);
                                } else {
                                    Toast.makeText(requireContext(), getString(R.string.spam_alert), Toast.LENGTH_SHORT).show();
                                }
                            }, Throwable::printStackTrace)
                    );
                }
            }
        });
    }

    private void setSocketOnListeners() {
        mSocket.on(SocketTagsHelper.USER_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject arg = (JSONObject) args[0];

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListOnView(arg);
                    }
                });
            }
        });

        mSocket.on(SocketTagsHelper.ALL_MESSAGES, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arg = (JSONArray) args[0];

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListOnView(arg);
                    }
                });
            }
        });
    }

    private void setListOnView(JSONObject arg) {
        try {
            addToMessageList(arg.getString("username"), arg.getString("message"), arg.getString("photo"));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            binding.fragmentMessageMessageList.setLayoutManager(new LinearLayoutManager(requireContext()));
            MessageAdapter adapter = new MessageAdapter(messageList);
            binding.fragmentMessageMessageList.setAdapter(adapter);
        }
    }

    private void setListOnView(JSONArray arg) {
        messageList.clear();

        try {
            for (int i = 0; i < arg.length(); i++) {
                JSONObject jsonObject = arg.getJSONObject(i);
                addToMessageList(jsonObject.getString("username"), jsonObject.getString("message"), jsonObject.getString("photo"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            binding.fragmentMessageMessageList.setLayoutManager(new LinearLayoutManager(requireContext()));
            MessageAdapter adapter = new MessageAdapter(messageList);
            binding.fragmentMessageMessageList.setAdapter(adapter);
        }
    }

    private void addToMessageList(String username, String message, String photo) {
        messageList.add(new MessageModel(username, photo, message));
    }
}