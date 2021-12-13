package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.ChattingMapper;
import com.yemektarifim.backend.model.Chatting;
import com.yemektarifim.backend.model.User;
import com.yemektarifim.backend.repository.ChattingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ChattingService {

    private final ChattingRepository chattingRepository;
    private final MessageService messageService;

    /**
     * @return Tüm sohbet listesi
     */
    public List<Chatting> getAll() {
        return chattingRepository.findAll();
    }

    /**
     * @param id Kullanıcının chattingId değeri
     * @return O id değerine sahip sohbet.
     */
    public Chatting getById(String id) {
        return chattingRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Chatting by Id %s", id)
        ));
    }

    /**
     * Veritabanına sohbeti kaydeder.
     */
    public void add(Chatting chatting) {
        chattingRepository.insert(chatting);
    }

    /**
     * Veritabanındaki bir sohbetin bilgilerini günceller.
     */
    public void update(Chatting chatting) {
        Chatting database = chattingRepository.findById(chatting.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Chatting by Id %s", chatting.getId())
                ));

        Chatting result = ChattingMapper.update(database, chatting);

        chattingRepository.save(result);
    }

    /**
     * Kullanıcıların sohbet değerini ve o sohbete ait mesajları siler.
     *
     * @param chattingId sohbet id değeri
     */
    public void deleteByChattingId(String chattingId) {
        messageService.deleteByChattingId(chattingId);
        chattingRepository.deleteById(chattingId);
    }

    /**
     * Sohbet edilen veya eden kişinin user id değerini içeren tüm sohbetler ve mesajlar silinir.
     *
     * @param userId Sohbet edilenlerden birisinin user id değeri.
     */
    public void deleteByUserId(String userId) {
        final List<Chatting> all = chattingRepository.findAll();

        for (Chatting chatting : all) {
            final Set<User> users = chatting.getUsers();

            for (User user : users) {
                if (user.getId().equals(userId)) {
                    deleteByChattingId(chatting.getId());
                }
            }
        }
    }
}
