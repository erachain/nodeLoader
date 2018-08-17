import com.sun.xml.internal.ws.util.StringUtils;
import crypto.Base58;
import utils.Pair;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;

public class LoadStarter {
    public static void main(String[] args) {
        Account account = new Account("8JutxFSoCa6opoytrjYZFMmBA1WGQpnS2y1cdDopvtmZ");
        System.out.println(account.getPublicKey());
        System.out.println(account.getPrivateKey());
        SendTX tx = new SendTX(account.getPublicKey(), "7Dpv5Gi8HjCBgtDN1P1niuPJQCBQ5H8Zob",
                "head text", "data text", BigDecimal.ZERO, System.currentTimeMillis(),0l,(byte)1);
        tx.sign(account.getKeysPair());
        System.out.println(Base58.encode(tx.toBytes(true)));
        //Account account = new Account(String.join("", seed));
        //byte[] publicKey = Base58.decode("7jH4P1inEhmm1Pm14NqbwiYgW19aFaYUVVNQTf1kn1w1");
        //byte[] privateKey = Base58.decode("eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHjuMfg8iKcSGab81b7g2pvCJiBYj1oVphRVU1BN1WSeXA11");
        //Pair<byte[], byte[]> keypair = new Pair<>(privateKey, publicKey);
        //try {
        //    System.out.println("signature:" + Base58.encode(crypto.Ed25519.sign(keypair, "test message".getBytes())));
        //} catch (NoSuchAlgorithmException e) {
        //    e.printStackTrace();
        //}
        //byte[] publicKey2 = Base58.decode("ukJFjo16eSa6i481mrnKrw8cSRjdqMVRCE966xB1t1s1");

       // System.out.println("Shared secret:" + Base58.encode(crypto.Ed25519.getSharedSecret(publicKey2, privateKey)));

        //System.out.println("Shared secret js check:" +
        //        Base58.encode(crypto.Ed25519.getSharedSecret(
        //                Base58.decode("492NHvVPQz1DoqqERX7aZ4YkyS5VUpWeaXsUGsAoNBpS"),
        //                Base58.decode("2mTwwdskkjghYhUHxfTqJZtwesCtcsVjH5W3DRMnMd67ED13xu9NawNri67k9949AH5krjDuoCor5xGBBKQ4RTyz"))));
        //try {
        //    KeyPair key = CryptoBouncyWrapper.generateGOSTKeyPair();
        //    System.out.println(Base58.encode(key.getPublic().getEncoded()).length() + ":" + Base58.encode(key.getPublic().getEncoded()));
        //    System.out.println(Base58.encode(key.getPrivate().getEncoded()).length()  + ":" + Base58.encode(key.getPrivate().getEncoded()));
        //} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
        //    e.printStackTrace();
        //}

    }
}
