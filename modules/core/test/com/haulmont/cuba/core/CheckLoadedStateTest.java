/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;

public class CheckLoadedStateTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void checkLocalProperties() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.LOCAL));

        entityStates.checkLoaded(user, "login", "name", "active");

        try {
            entityStates.checkLoaded(user, "group");

            Assert.fail("user.group is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("group is not loaded"));
        }
    }

    @Test
    public void checkMinimalProperties() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        entityStates.checkLoaded(user, "login", "name");

        try {
            entityStates.checkLoaded(user, "group");

            Assert.fail("user.group is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("group is not loaded"));
        }

        try {
            entityStates.checkLoaded(user, "password");

            Assert.fail("user.password is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("password is not loaded"));
        }

        try {
            entityStates.checkLoaded(user, "email");

            Assert.fail("user.email is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("email is not loaded"));
        }
    }

    @Test
    public void checkLocalView() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.LOCAL));

        entityStates.checkLoadedView(user, View.LOCAL);

        User userMinimal = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        try {
            entityStates.checkLoadedView(userMinimal, View.LOCAL);

            Assert.fail("user local attributes are not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString(" is not loaded"));
        }
    }

    @Test
    public void checkDeepView() {
        // todo
    }

    @Test
    public void checkRelatedView() {
        // todo
    }
}