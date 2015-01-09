/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile.ui.gist;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mobile.R;
import com.github.mobile.ui.ItemListAdapter;
import com.github.mobile.ui.StyledText;
import com.github.mobile.util.AvatarLoader;

import java.text.NumberFormat;
import java.util.List;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.User;

/**
 * Adapter to display a list of {@link Gist} objects
 */
public class GistListAdapter extends ItemListAdapter<Gist, GistView> {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat
        .getIntegerInstance();

    private final AvatarLoader avatars;

    private String anonymous;

    /**
     * @param avatars
     * @param inflater
     * @param elements
     */
    public GistListAdapter(AvatarLoader avatars, LayoutInflater inflater,
        List<Gist> elements) {
        super(R.layout.gist_item, inflater, elements);

        this.avatars = avatars;
        setItems(elements);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position).getId();
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected void update(final int position, final GistView view,
        final Gist gist) {
        view.gistId.setText(gist.getId());

        String description = gist.getDescription();
        if (!TextUtils.isEmpty(description))
            view.title.setText(description);
        else
            view.title.setText(R.string.no_description_given);

        User user = gist.getUser();
        avatars.bind(view.avatar, user);

        StyledText authorText = new StyledText();
        if (user != null)
            authorText.bold(user.getLogin());
        else
            authorText.bold(anonymous);
        authorText.append(' ');
        authorText.append(gist.getCreatedAt());
        view.author.setText(authorText);

        view.files.setText(NUMBER_FORMAT.format(gist.getFiles().size()));
        view.comments.setText(NUMBER_FORMAT.format(gist.getComments()));
    }

    @Override
    protected GistView createView(final View view) {
        return new GistView(view);
    }
}
