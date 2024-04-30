package com.example.newsfeedmodule.Controller;

import com.example.newsfeedmodule.Dto.NewsFeedDto;
import com.example.newsfeedmodule.Service.NewsFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/newsfeed")
public class NewsFeedController {

    private final NewsFeedService newsFeedService;
    @GetMapping("/feed")
    public ResponseEntity<List<NewsFeedDto>> getNewsFeeds(@RequestParam Long userId, @RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") int size) {
        List<NewsFeedDto> newsFeeds = newsFeedService.getNewsFeedsWithCursor(userId, cursorId, size);
        return ResponseEntity.ok(newsFeeds);
    }
}
