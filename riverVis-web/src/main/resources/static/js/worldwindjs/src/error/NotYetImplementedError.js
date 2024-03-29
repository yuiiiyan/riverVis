/*
 * Copyright 2015-2017 WorldWind Contributors
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
/**
 * @exports NotYetImplementedError
 */
define(['./AbstractError'],
    function (AbstractError) {
        "use strict";

        /**
         * Constructs a not-yet-implemented error with a specified message.
         * @alias NotYetImplementedError
         * @constructor
         * @classdesc Represents an error associated with an operation that is not yet implemented.
         * @augments AbstractError
         * @param {String} message The message.
         */
        var NotYetImplementedError = function (message) {
            AbstractError.call(this, "NotYetImplementedError", message);

            var stack;
            try {
                //noinspection ExceptionCaughtLocallyJS
                throw new Error();
            } catch (e) {
                stack = e.stack;
            }
            this.stack = stack;
        };

        NotYetImplementedError.prototype = Object.create(AbstractError.prototype);

        return NotYetImplementedError;
    });