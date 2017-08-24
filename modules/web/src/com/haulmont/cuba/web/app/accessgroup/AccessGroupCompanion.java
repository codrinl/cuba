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

package com.haulmont.cuba.web.app.accessgroup;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.app.security.group.browse.GroupBrowser;
import com.haulmont.cuba.gui.app.security.group.browse.UserGroupChangedEvent;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.ui.AbstractSelect;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class AccessGroupCompanion implements GroupBrowser.Companion {

    @Override
    public void initDragAndDrop(Table<User> usersTable, Tree<Group> groupsTree, Consumer<UserGroupChangedEvent> userGroupChangedHandler) {
        com.vaadin.ui.Table vTable = usersTable.unwrap(com.vaadin.ui.Table.class);
        vTable.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);

        CubaTree vTree = groupsTree.unwrap(CubaTree.class);
        vTree.setDragMode(com.vaadin.ui.Tree.TreeDragMode.NODE);
        vTree.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                AbstractSelect.AbstractSelectTargetDetails dropData =
                        ((AbstractSelect.AbstractSelectTargetDetails) dropEvent.getTargetDetails());

                Object itemId = transferable.getItemId();
                Container sourceContainer = transferable.getSourceContainer();
                User user = convertToEntity(sourceContainer.getItem(itemId), User.class);
                if (user == null) {
                    return;
                }

                final Object targetItemId = dropData.getItemIdOver();
                if (targetItemId == null) {
                    return;
                }
                Group group = convertToEntity(vTree.getItem(targetItemId), Group.class);
                if (group == null) {
                    return;
                }

                userGroupChangedHandler.accept(new UserGroupChangedEvent(groupsTree, user, group));
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new And(AbstractSelect.AcceptItem.ALL);
            }
        });
    }

    @Nullable
    private <T extends Entity> T convertToEntity(@Nullable Item item, Class<T> entityClass) {
        if (!(item instanceof ItemWrapper)) {
            return null;
        }
        Entity entity = ((ItemWrapper) item).getItem();
        if (!entityClass.isAssignableFrom(entity.getClass())) {
            return null;
        }
        //noinspection unchecked
        return (T) entity;
    }
}
