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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class ClickDispatcher extends ViewInjector implements View.OnClickListener {

    private static final String INVALID_METHOD_SIGNATURE = "Method signature [%s] does not match required signature [void methodName(View)]";
    private static final String CANNOT_DISPATCH_CLICK = "Failed to dispatch click to %s";

    private final HashMap<View, Method> clickListeners;
    private final Activity activity;

    public ClickDispatcher(Activity activity) {
        this.activity = activity;
        clickListeners = new HashMap<View, Method>();
    }

    public void injectListeners() {
        List<Method> methods = getAttachableMethods();
        for (Method method : methods) {
            validateSignature(method);
            HandleClickOn handleAnnotation = method.getAnnotation(HandleClickOn.class);
            int viewId = handleAnnotation.value();
            String tag = handleAnnotation.tag();
            View v = findView(activity, viewId, tag);
            validateView(v, viewId, tag, HandleClickOn.class.getSimpleName(), method.getName());
            addListener(v, method);
            v.setOnClickListener(this);
        }
    }

    private void validateSignature(Method method) {
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 1 || !View.class.isAssignableFrom(params[0]) ||
                !method.getReturnType().equals(void.class)) {
            throw new IllegalArgumentException(String.format(INVALID_METHOD_SIGNATURE, method.toGenericString()));
        }
    }

    private void addListener(View view, Method method) {
        clickListeners.put(view, method);
    }

    private List<Method> getAttachableMethods() {
        Method[] methods = activity.getClass().getDeclaredMethods();
        List<Method> result = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(HandleClickOn.class)) {
                result.add(method);
            }
        }
        return result;
    }


    @Override
    public void onClick(View view) {
        if (clickListeners.containsKey(view)) {
            Method method = clickListeners.get(view);
            try {
                method.invoke(activity, view);
            } catch (Exception e) {
                throw new IllegalStateException(String.format(CANNOT_DISPATCH_CLICK, method.toGenericString()), e);
            }
        }
    }
}
