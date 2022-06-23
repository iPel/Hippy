/*
 * Tencent is pleased to support the open source community by making
 * Hippy available.
 *
 * Copyright (C) 2017-2019 THL A29 Limited, a Tencent company.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma once

#include <memory>
#include "api/notification/devtools_log_notification.h"
#include "api/notification/devtools_network_notification.h"
#include "api/notification/devtools_runtime_notification.h"
#include "api/notification/devtools_vm_response_notification.h"
#include "api/notification/devtools_dom_tree_notification.h"

namespace hippy::devtools {

 /**
  * @brief Devtools provide a set of notification interfaces to notify frontend by the external access framework,
  * like Debug log, debug response, etc
  */
struct NotificationCenter {
  std::shared_ptr<LogNotification> log_notification;
  std::shared_ptr<VmResponseNotification> vm_response_notification;
  std::shared_ptr<NetworkNotification> network_notification;
  std::shared_ptr<RuntimeNotification> runtime_notification;
  std::shared_ptr<DomTreeNotification> dom_tree_notification;
};
}  // namespace hippy::devtools
