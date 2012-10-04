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

class ViewInjector {
    protected static final String EMPTY_TAG = "";
    private static final String INVALID_VIEW_ID = "Invalid view id (%s) in @%s for [%s]";
    private static final String INVALID_VIEW_TAG = "Invalid tag (%s) in @%s for [%s]";

    protected View findView(View parent, int viewId, String tag) {
        View v = null;
        if (viewId != -1) {
            v = parent.findViewById(viewId);
        } else if (!EMPTY_TAG.equals(tag)) {
            v = parent.findViewWithTag(tag);
        }
        return v;
    }

    protected void validateView(View v, int viewId, String tag, String annotationName, String fieldName) {
        if (v == null) {
            IllegalArgumentException ex;
            if (viewId == -1 && !EMPTY_TAG.equals(tag)) {
                ex = new IllegalArgumentException(String.format(INVALID_VIEW_TAG, tag, annotationName, fieldName));
            } else {
                ex = new IllegalArgumentException(String.format(INVALID_VIEW_ID, viewId, annotationName, fieldName));
            }
            throw ex;
        }
    }

}
