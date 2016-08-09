/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.reactivex.android.plugins;

import io.reactivex.Scheduler;
import io.reactivex.android.testutil.EmptyScheduler;
import io.reactivex.functions.Function;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public final class RxAndroidPluginsTest {
    @Before @After
    public void setUpAndTearDown() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void mainThreadHandlerCalled() {
        final AtomicReference<Scheduler> schedulerRef = new AtomicReference<>();
        final Scheduler newScheduler = new EmptyScheduler();
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override public Scheduler apply(Scheduler scheduler) {
                schedulerRef.set(scheduler);
                return newScheduler;
            }
        });

        Scheduler scheduler = new EmptyScheduler();
        Scheduler actual = RxAndroidPlugins.onMainThreadScheduler(scheduler);
        assertSame(newScheduler, actual);
        assertSame(scheduler, schedulerRef.get());
    }

    @Test
    public void resetClearsMainThreadHandler() {
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override public Scheduler apply(Scheduler scheduler) {
                throw new RuntimeException();
            }
        });
        RxAndroidPlugins.reset();

        Scheduler scheduler = new EmptyScheduler();
        Scheduler actual = RxAndroidPlugins.onMainThreadScheduler(scheduler);
        assertSame(scheduler, actual);
    }

    @Test
    public void initMainThreadHandlerCalled() {
        final AtomicReference<Scheduler> schedulerRef = new AtomicReference<>();
        final Scheduler newScheduler = new EmptyScheduler();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override public Scheduler apply(Scheduler scheduler) {
                schedulerRef.set(scheduler);
                return newScheduler;
            }
        });

        Scheduler scheduler = new EmptyScheduler();
        Scheduler actual = RxAndroidPlugins.initMainThreadScheduler(scheduler);
        assertSame(newScheduler, actual);
        assertSame(scheduler, schedulerRef.get());
    }

    @Test
    public void resetClearsInitMainThreadHandler() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override public Scheduler apply(Scheduler scheduler) {
                throw new RuntimeException();
            }
        });
        RxAndroidPlugins.reset();

        Scheduler scheduler = new EmptyScheduler();
        Scheduler actual = RxAndroidPlugins.initMainThreadScheduler(scheduler);
        assertSame(scheduler, actual);
    }
}
