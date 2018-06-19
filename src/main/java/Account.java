import com.google.common.primitives.Bytes;
import crypto.Base58;
import utils.Pair;

import java.nio.charset.StandardCharsets;

public class Account {

    private String address;
    private String seed;
    private byte[] seedByte;
    private Pair<byte[], byte[]> keysPair;

    Account(String seed) {
        this.seed = seed;
        this.seedByte = Base58.decode(seed);
        this.keysPair = crypto.Ed25519.createKeyPair(this.seedByte);
        this.address = crypto.Crypto.getInstance().getAddress(this.keysPair.getB());
    }

    public String getAddress() {
        return this.address;
    }

    public String getPublicKey() {
        return Base58.encode(this.keysPair.getB());
    }
}
