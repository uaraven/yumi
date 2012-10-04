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

public class Injector {

    public static void injectActivity(Activity activity) {
        injectLayout(activity);
        injectFields(activity);
        injectListeners(activity);
    }

    private static void injectLayout(Activity activity) {
        LayoutInjector injector = new LayoutInjector(activity);
        injector.inject();
    }

    public static void injectFields(Activity activity) {
        FieldInjector injector = new FieldInjector(activity);

        injector.attachViews();
    }

    public static void injectListeners(Activity activity) {
        ClickDispatcher dispatcher = new ClickDispatcher(activity);
        dispatcher.injectListeners();
    }
}
