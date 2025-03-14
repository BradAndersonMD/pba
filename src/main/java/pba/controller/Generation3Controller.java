package pba.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pba.models.replay.results.Generation3Results;
import pba.service.replay.Generation3ReplayService;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController("generation3")
public class Generation3Controller {

    private final Generation3ReplayService replayService;

    @PostMapping("/analyze")
    Mono<Generation3Results> analyze(@RequestBody List<String> replayUrls) {
        return replayService.process(replayUrls);
    }

}
