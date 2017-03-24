package meta;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.explod.querydb.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Config.ALL_SDKS)
public abstract class BaseRoboTest {
}
