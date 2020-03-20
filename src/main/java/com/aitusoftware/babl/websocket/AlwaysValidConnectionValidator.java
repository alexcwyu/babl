/*
 * Copyright 2019-2020 Aitu Software Limited.
 *
 * https://aitusoftware.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aitusoftware.babl.websocket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * {@code ConnectionValidator} that will always succeed.
 */
public final class AlwaysValidConnectionValidator implements ConnectionValidator
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void validateConnection(
        final ValidationResult validationResult,
        final Consumer<BiConsumer<CharSequence, CharSequence>> headerProvider,
        final ValidationResultPublisher validationResultPublisher)
    {
        validationResult.validationSuccess();
        validationResultPublisher.publishResult(validationResult);
    }
}