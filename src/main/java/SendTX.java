import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import io.github.novacrypto.base58.Base58;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class SendTX {

    private static final int KEY_LENGTH = 8;
    private static final int AMOUNT_LENGTH = 8;
    private static final int AMOUNT_DEFAULT_SCALE = 8;
    private static final int TIMESTAMP_LENGTH = 8;
    private static final int IS_TEXT_LENGTH = 1;
    private static final int DATA_SIZE_LENGTH = 4;
    private static final int ENCRYPTED_LENGTH = 1;
    private static final int TYPE_LENGTH = 4;
    private static final int HASH_LENGTH = 32;
    private byte[] encrypted;
    private byte[] isText;
    private byte[] creator;
    private byte[] recipient;
    private byte[] type;
    private byte[] signature;
    private String head;
    private String data;
    private BigDecimal amount;
    private long timestamp;
    private long reference;
    private long key;
    private byte feePow;

    SendTX(byte[] data) {
        this.parseTX(data);
    }

    SendTX(String creator, String recipient, String head, String data, BigDecimal amount, long timestamp,
           long key, byte feePow) {
        byte[] type = new byte[4];
        if (amount.compareTo(new BigDecimal(0)) > 0) {
            type[2] = (byte) 0;
        } else {
            type[2] = (byte) -127;
        }
        if ( head != null || data != null ) {
            type[3] = (byte) 0;
        } else {
            type[2] = (byte) -127;
        }
        this.setTX((byte) 0, (byte) 0, creator, recipient, type, head, data, amount, timestamp, key, feePow);
    }

    private void setTX(byte encrypted, byte isText, String creator, String recipient, byte[] type, String head,
                       String data, BigDecimal amount, long timestamp, long key, byte feePow) {
        this.type = type;
        this.timestamp = timestamp;
        this.reference = (long) 0;
        this.creator = Base58.base58Decode(creator);
        this.feePow = feePow;
        this.recipient = Base58.base58Decode(recipient);
        this.key = key;
        this.amount = amount;
        this.head = head;
        this.data = data;
        this.encrypted = new byte[] {encrypted};
        this.isText = new byte[] {isText};
    }

    private void parseTX(byte[] data) {

        // READ TYPE
        this.type = Arrays.copyOfRange(data, 0, TYPE_LENGTH);
        int position = TYPE_LENGTH;

        // READ TIMESTAMP
        byte[] timestampBytes = Arrays.copyOfRange(data, position, position + TIMESTAMP_LENGTH);
        this.timestamp = Longs.fromByteArray(timestampBytes);
        position += TIMESTAMP_LENGTH;

        // READ REFERENCE
        byte[] referenceBytes = Arrays.copyOfRange(data, position, position + TIMESTAMP_LENGTH);
        this.reference = Longs.fromByteArray(referenceBytes);
        position += TIMESTAMP_LENGTH;

        // READ CREATOR
        this.creator = Arrays.copyOfRange(data, position, position + HASH_LENGTH);
        position += HASH_LENGTH;

        // READ FEE POWER
        byte[] feePowBytes = Arrays.copyOfRange(data, position, position + 1);
        this.feePow = feePowBytes[0];
        position += 1;

        // READ SIGNATURE
        this.signature = Arrays.copyOfRange(data, position, position + HASH_LENGTH);
        position += HASH_LENGTH;

        // READ RECIPIENT
        this.recipient = Arrays.copyOfRange(data, position, position + crypto.Crypto.ADDRESS_LENGTH);
        position += crypto.Crypto.ADDRESS_LENGTH;

        if (this.type[2] >= 0) {
            // IF here is AMOUNT

            // READ KEY
            byte[] keyBytes = Arrays.copyOfRange(data, position, position + KEY_LENGTH);
            this.key = Longs.fromByteArray(keyBytes);
            position += KEY_LENGTH;

            // READ AMOUNT
            byte[] amountBytes = Arrays.copyOfRange(data, position, position + AMOUNT_LENGTH);
            this.amount = new BigDecimal(new BigInteger(amountBytes), AMOUNT_DEFAULT_SCALE);
            position += AMOUNT_LENGTH;
        }

        // HEAD LEN
        byte headLen = data[position];
        position++;

        // HEAD
        byte[] headBytes = Arrays.copyOfRange(data, position, position + headLen);
        this.head = new String(headBytes, StandardCharsets.UTF_8);
        position += headLen;

        // DATA +++
        if (this.type[3] >= 0) {
            // IF here is DATA
            // READ DATA SIZE
            byte[] dataSizeBytes = Arrays.copyOfRange(data, position, position + DATA_SIZE_LENGTH);
            int dataSize = Ints.fromByteArray(dataSizeBytes);
            position += DATA_SIZE_LENGTH;

            // READ DATA
            byte[] dataBytes = Arrays.copyOfRange(data, position, position + dataSize);
            this.data = new String(dataBytes, StandardCharsets.UTF_8);
            position += dataSize;

            // READ ENCRYPTED FLAG
            this.encrypted = Arrays.copyOfRange(data, position, position + ENCRYPTED_LENGTH);
            position += ENCRYPTED_LENGTH;

            this.isText = Arrays.copyOfRange(data, position, position + IS_TEXT_LENGTH);
            //position += IS_TEXT_LENGTH;
        }
    }

    public byte[] toBytes(boolean withSign) {

        byte[] data = new byte[0];

        // WRITE TYPE
        data = Bytes.concat(data, this.type);

        // WRITE TIMESTAMP
        byte[] timestampBytes = Longs.toByteArray(this.timestamp);
        timestampBytes = Bytes.ensureCapacity(timestampBytes, TIMESTAMP_LENGTH, 0);
        data = Bytes.concat(data, timestampBytes);

        // refrence field is unused
        byte[] referenceBytes = Longs.toByteArray(this.reference);
        referenceBytes = Bytes.ensureCapacity(referenceBytes, TIMESTAMP_LENGTH, 0);
        data = Bytes.concat(data, referenceBytes);

        // WRITE CREATOR
        data = Bytes.concat(data, this.creator);

        // WRITE FEE POWER
        byte[] feePowBytes = new byte[1];
        feePowBytes[0] = this.feePow;
        data = Bytes.concat(data, feePowBytes);

        // SIGNATURE
        if (withSign)
            data = Bytes.concat(data, this.signature);

        // WRITE RECIPIENT
        data = Bytes.concat(data, this.recipient);

        if (this.amount != null) {

            // WRITE KEY
            byte[] keyBytes = Longs.toByteArray(2L);
            keyBytes = Bytes.ensureCapacity(keyBytes, KEY_LENGTH, 0);
            data = Bytes.concat(data, keyBytes);

            //WRITE AMOUNT
            byte[] amountBytes = Longs.toByteArray(this.amount.unscaledValue().longValue());
            amountBytes = Bytes.ensureCapacity(amountBytes, AMOUNT_LENGTH, 0);
            data = Bytes.concat(data, amountBytes);

        }

        // WRITE HEAD
        byte[] headBytes = this.head.getBytes(StandardCharsets.UTF_8);
        // HEAD SIZE
        data = Bytes.concat(data, new byte[]{(byte) headBytes.length});
        // HEAD
        data = Bytes.concat(data, headBytes);

        if (this.data != null) {
            // WRITE DATA SIZE
            byte[] dataBytes = this.data.getBytes(StandardCharsets.UTF_8);
            byte[] dataSizeBytes = Ints.toByteArray(dataBytes.length);
            data = Bytes.concat(data, dataSizeBytes);

            // WRITE DATA
            data = Bytes.concat(data, dataBytes);

            // WRITE ENCRYPTED
            data = Bytes.concat(data, this.encrypted);

            // WRITE ISTEXT
            data = Bytes.concat(data, this.isText);
        }

        return data;
    }
}
