/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Artipie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.pypi.http;

import com.artipie.http.Headers;
import com.artipie.http.headers.ContentType;
import io.reactivex.Flowable;
import java.util.Collections;
import java.util.concurrent.CompletionException;
import org.cactoos.list.ListOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.AllOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.MatcherOf;

/**
 * Test for {@link Multipart}.
 * @since 0.1
 */
class MultipartTest {

    @Test
    void failsIfHeadersAreEmpty() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                CompletionException.class,
                () -> new Multipart(Collections.emptyList(), Flowable.empty())
                    .content().toCompletableFuture().join()
            ).getCause(),
            new AllOf<>(
                new ListOf<>(
                    new MatcherOf<>(ex -> ex instanceof IllegalStateException),
                    new MatcherOf<>(
                        ex -> {
                            return ex.getMessage().contains("Cannot find header \"Content-Type\"");
                        })
                )
            )
        );
    }

    @Test
    void failsIfBoundaryIsNotProvided() {
        MatcherAssert.assertThat(
            Assertions.assertThrows(
                CompletionException.class,
                () -> new Multipart(
                    new Headers.From(new ContentType("123")), Flowable.empty()
                ).content().toCompletableFuture().join()
            ).getCause(),
            new AllOf<>(
                new ListOf<>(
                    new MatcherOf<>(ex -> ex instanceof NullPointerException),
                    new MatcherOf<>(
                        ex -> {
                            return ex.getMessage().contains("Boundary not specified");
                        })
                )
            )
        );
    }

}
