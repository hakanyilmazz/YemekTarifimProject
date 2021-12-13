package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.Message;
import com.yemektarifim.backend.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    /**
     * @return Veritabanındaki tüm mesajların listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Message>> getAll() {
        List<Message> messageList = messageService.getAll();
        return ResponseEntity.ok(messageList);
    }

    /**
     * @return Id değerine göre bir mesajı döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> getById(@PathVariable String id) {
        Message message = messageService.getById(id);
        return ResponseEntity.ok(message);
    }

    /**
     * @return Sohbet Id değerine göre mesajları döndürür.
     */
    @GetMapping("/{chattingId}")
    public ResponseEntity<List<Message>> getByChattingId(@PathVariable String chattingId) {
        List<Message> messageList = messageService.getByChattingId(chattingId);
        return ResponseEntity.ok(messageList);
    }

    /**
     * Veritabanına yeni bir mesaj nesnesi ekler.
     */
    @PostMapping
    public ResponseEntity<Message> add(@RequestBody Message message) {
        messageService.add(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Veritabanındaki bir mesaj bilgisini günceller.
     */
    @PutMapping
    public ResponseEntity<Message> update(@RequestBody Message message) {
        messageService.update(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Veritabanından bir mesajı siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteById(@PathVariable String id) {
        messageService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Veritabanından kullanıcıların sohbet id'sine göre ait mesajları siler.
     * Örneğin: İki kişi arasındaki sohbet mesajlarını siler.
     * (İkisi için en başta bir sohbet id'si üretilir. Bu id sohbet mesajlarının kimliğini belirler.)
     */
    @DeleteMapping("/{chattingId}")
    public ResponseEntity<Message> deleteByChattingId(@PathVariable String chattingId) {
        messageService.deleteByChattingId(chattingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
