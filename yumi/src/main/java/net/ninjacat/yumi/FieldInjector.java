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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

final class FieldInjector extends ViewInjector {

    private static final String CANNOT_ATTACH_TO_NON_VIEW = "Cannot attach view to field (%s:%s) not descending from android.view.View";
    private View view;
    private Object target;


    public FieldInjector(View view, Object target) {
        this.view = view;
        this.target = target;
    }

    void attachViews() {
        List<Field> injectableFields = getInjectableFields();

        for (Field field : injectableFields) {
            AttachTo attachAnnotation = field.getAnnotation(AttachTo.class);
            int viewId = attachAnnotation.value();
            String tag = attachAnnotation.tag();
            View v = findView(view, viewId, tag);
            validateView(v, viewId, tag, AttachTo.class.getSimpleName(), field.getName());
            if (!(View.class.isAssignableFrom(field.getType()))) {
                throw new IllegalStateException(String.format(CANNOT_ATTACH_TO_NON_VIEW, field.getName(), field.getType().getSimpleName()));
            }
            try {
                field.setAccessible(true);
                field.set(target, v);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }


    private List<Field> getInjectableFields() {
        Field[] fields = target.getClass().getDeclaredFields();
        List<Field> result = new ArrayList<Field>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AttachTo.class)) {
                result.add(field);
            }
        }
        return result;
    }

}
