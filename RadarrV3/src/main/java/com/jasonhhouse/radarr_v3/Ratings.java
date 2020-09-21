/*
 *
 *  Copyright 2020 Jason H House
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jasonhhouse.radarr_v3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Ratings {
    @NotNull
    private final Integer votes;
    @NotNull
    private final Double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Ratings(@JsonProperty(value = "votes") @Nullable Integer votes,
                   @JsonProperty(value = "value") @Nullable Double value) {
        this.votes = votes == null ? -1 : votes;
        this.value = value == null ? -1.0 : value;
    }

    @NotNull
    static Ratings getDefault() {
        return new Ratings(null, null);
    }

    @NotNull
    public Integer getVotes() {
        return votes;
    }

    @NotNull
    public Double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ratings ratings = (Ratings) o;
        return votes.equals(ratings.votes) &&
                value.equals(ratings.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(votes, value);
    }

    @Override
    public String toString() {
        return "Ratings{" +
                "votes=" + votes +
                ", value=" + value +
                '}';
    }
}
