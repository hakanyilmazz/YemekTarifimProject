package com.yemektarifim.backend.controller;

import com.yemektarifim.backend.model.Chatting;
import com.yemektarifim.backend.service.ChattingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chatting")
public class ChattingController {

    private final ChattingService chattingService;

    /**
     * @return Tüm sohbet listesini döndürür.
     */
    @GetMapping
    public ResponseEntity<List<Chatting>> getAll() {
        List<Chatting> chattingList = chattingService.getAll();
        return ResponseEntity.ok(chattingList);
    }

    /**
     * @return Belirli bir sohbeti chatting id değerine göre döndürür.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Chatting> getById(@PathVariable String id) {
        Chatting chatting = chattingService.getById(id);
        return ResponseEntity.ok(chatting);
    }

    /**
     * Parametre ile verilen sohbeti veritabanına kaydeder.
     */
    @PostMapping
    public ResponseEntity<Chatting> add(@RequestBody Chatting chatting) {
        chattingService.add(chatting);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Verilen sohbet nesnesini veritabanında günceller.
     */
    @PutMapping
    public ResponseEntity<Chatting> update(@RequestBody Chatting chatting) {
        chattingService.update(chatting);
        return ResponseEntity.ok().build();
    }

    /**
     * Sohbeti siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Chatting> deleteById(@PathVariable String id) {
        chattingService.deleteByUserId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
