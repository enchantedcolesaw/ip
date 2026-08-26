package gatsby.command;

import gatsby.testutil.RecordingUi;
import gatsby.testutil.TestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;

/** Shares filesystem isolation and UI setup for command tests. */
abstract class AbstractCommandTest {
    private Path dataBackup;

    @BeforeEach
    void isolateDataDirectory() throws Exception {
        dataBackup = TestSupport.isolateDataDirectory();
    }

    @AfterEach
    void restoreDataDirectory() throws Exception {
        TestSupport.restoreDataDirectory(dataBackup);
    }

    /** Creates a UI double that records messages instead of printing them. */
    protected RecordingUi recordingUi() {
        return new RecordingUi();
    }
}
