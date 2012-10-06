/********************************************************************************
 *       Copyright 2012 Oleksiy Voronin <ovoronin@gmail.com>
 *       http://ninjacat.net
 *       ==============================
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 *********************************************************************************/
package net.ninjacat.yumi;

import android.app.Activity;
import android.view.View;

public class Yumi {

    /**
     * Attach fields annotated with {@link AttachTo} to layout views. Configure OnClickListener dispatcher to
     * send click events to methods annotated with {@link HandleClickOn}
     *
     * @param view   parent view, in which to search for views to inject
     * @param target object, which fields and methods to perform injection into
     */
    public static void inject(View view, Object target) {
        injectFields(view, target);
        injectListeners(view, target);
    }

    /**
     * Set content view for activity (if activity is annotated with {@link Layout}), then
     * attach fields annotated with {@link AttachTo} to layout views. Configure OnClickListener dispatcher to
     * send click events to methods annotated with {@link HandleClickOn}
     *
     * @param activity - activity on which to perform injection
     */
    public static void injectActivity(Activity activity) {
        injectLayout(activity);
        View rootView = activity.getWindow().getDecorView();
        inject(rootView, activity);
    }

    private static void injectLayout(Activity activity) {
        LayoutInjector injector = new LayoutInjector(activity);
        injector.inject();
    }

    private static void injectFields(View view, Object target) {
        FieldInjector injector = new FieldInjector(view, target);

        injector.attachViews();
    }

    private static void injectListeners(View view, Object target) {
        ClickDispatcher dispatcher = new ClickDispatcher(view, target);
        dispatcher.injectListeners();
    }
}
