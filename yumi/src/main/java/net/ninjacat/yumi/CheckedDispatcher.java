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

import android.view.View;
import android.widget.CompoundButton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class CheckedDispatcher extends ViewInjector implements CompoundButton.OnCheckedChangeListener {

    private static final String INVALID_METHOD_SIGNATURE = "Method signature [%s] does not match required signature [void methodName(CounpondButton, boolean)]";
    private static final String CANNOT_DISPATCH_CLICK = "Failed to dispatch click to %s";
    private static final String INVALID_VIEW_TYPE = "Supplied view is not CompoundButton and cannot be handled by [%s]";

    private final HashMap<CompoundButton, Method> checkedChangeListeners;
    private final View view;
    private final Object target;

    CheckedDispatcher(View view, Object target) {
        this.view = view;
        this.target = target;
        checkedChangeListeners = new HashMap<CompoundButton, Method>();
    }

    public void injectListeners() {
        List<Method> methods = getAttachableMethods();
        for (Method method : methods) {
            validateSignature(method);
            HandleCheckedChange handleAnnotation = method.getAnnotation(HandleCheckedChange.class);
            int viewId = handleAnnotation.value();
            String tag = handleAnnotation.tag();
            View v = findView(view, viewId, tag);
            if (!(v instanceof CompoundButton)) {
                throw new IllegalArgumentException(String.format(INVALID_VIEW_TYPE, method.toGenericString()));
            }
            CompoundButton cb = (CompoundButton) v;
            validateView(cb, viewId, tag, HandleCheckedChange.class.getSimpleName(), method.getName());
            addListener(cb, method);
            cb.setOnCheckedChangeListener(this);
        }
    }

    private void validateSignature(Method method) {
        Class<?>[] params = method.getParameterTypes();
        if ((params.length != 2) || !CompoundButton.class.isAssignableFrom(params[0]) ||
                !boolean.class.isAssignableFrom(params[1]) || !method.getReturnType().equals(void.class)) {
            throw new IllegalArgumentException(String.format(INVALID_METHOD_SIGNATURE, method.toGenericString()));
        }
    }

    private void addListener(CompoundButton view, Method method) {
        checkedChangeListeners.put(view, method);
    }

    private List<Method> getAttachableMethods() {
        Method[] methods = target.getClass().getDeclaredMethods();
        List<Method> result = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(HandleCheckedChange.class)) {
                result.add(method);
            }
        }
        return result;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (checkedChangeListeners.containsKey(compoundButton)) {
            Method method = checkedChangeListeners.get(compoundButton);
            try {
                method.invoke(target, compoundButton, b);
            } catch (Exception e) {
                throw new IllegalStateException(String.format(CANNOT_DISPATCH_CLICK, method.toGenericString()), e);
            }
        }
    }
}
