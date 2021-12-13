package com.yemektarifim.backend.service;

import com.yemektarifim.backend.mapper.MessageMapper;
import com.yemektarifim.backend.model.Message;
import com.yemektarifim.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    /**
     * @return Veritabanındaki tüm mesajların listesi
     */
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    /**
     * @param id mesajın id değeri
     * @return O id değerine sahip mesaj.
     */
    public Message getById(String id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException(
                String.format("Cannot Find Message by Id %s", id)
        ));
    }

    /**
     * Veritabanına message nesnesini kaydeder.
     */
    public void add(Message message) {
        messageRepository.insert(message);
    }

    /**
     * Veritabanındaki bir message nesnesinin bilgilerini günceller.
     */
    public void update(Message message) {
        Message database = messageRepository.findById(message.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find Message by Id %s", message.getId())
                ));

        Message result = MessageMapper.update(database, message);

        messageRepository.save(result);
    }

    /**
     * Veritabanından bir mesaj nesnesini siler.
     *
     * @param id silinecek olan mesaj objesinin id değeri.
     */
    public void deleteById(String id) {
        messageRepository.deleteById(id);
    }

    /**
     * Sohbet id değerine göre mesajları siler.
     *
     * @param chattingId Mesajları silinecek sohbet id değeri.
     */
    public void deleteByChattingId(String chattingId) {
        messageRepository.deleteByChattingId(chattingId);
    }

    /**
     * Sohbet id değerine göre mesajları liste olarak döndürür..
     *
     * @param chattingId Mesajları listelenecek sohbet id değeri.
     * @return belirli bir sohbete ait mesaj listesi
     */
    public List<Message> getByChattingId(String chattingId) {
        return messageRepository.findByChattingId(chattingId);
    }
}
