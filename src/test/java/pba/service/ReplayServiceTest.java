package pba.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pba.client.ShowdownClient;

@ExtendWith(MockitoExtension.class)
class ReplayServiceTest {

    @Mock
    private ShowdownClient showdownClient;
    @Mock
    private ReplayFileReader replayFileReader;

    @InjectMocks
    private ReplayService replayService;

    @Test
    void itShouldParseAReplay() {



    }

}
