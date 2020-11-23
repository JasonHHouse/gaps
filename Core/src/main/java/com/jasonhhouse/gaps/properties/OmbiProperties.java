package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OmbiProperties {

    @NotNull
    private final String address;

    @NotNull
    private final Integer port;

    @NotNull
    private final String apiKey;

    public OmbiProperties(@JsonProperty(value = "address") @Nullable String address,
                          @JsonProperty(value = "port") @Nullable Integer port,
                          @JsonProperty(value = "apiKey") @Nullable String apiKey) {
        this.address = address == null ? "" : address;
        this.port = port == null ? -1 : port;
        this.apiKey = apiKey == null ? "" : apiKey;
    }

    static OmbiProperties getDefault() {
        return new OmbiProperties("", -1, "");
    }

    public @NotNull String getAddress() {
        return address;
    }

    public @NotNull Integer getPort() {
        return port;
    }

    public @NotNull String getApiKey() {
        return apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OmbiProperties that = (OmbiProperties) o;
        return address.equals(that.address) &&
                port.equals(that.port) &&
                apiKey.equals(that.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port, apiKey);
    }

    @Override
    public String toString() {
        return "OmbiProperties{" +
                "address='" + address + '\'' +
                ", port=" + port +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
