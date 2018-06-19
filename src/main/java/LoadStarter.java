import crypto.Base58;
import crypto.CryptoBouncyWrapper;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class LoadStarter {
    public static void main(String[] args) {
        Account account = new Account("9hJdF6domrkqCKghgWYFX9JC7MUPFdzNA84Gca3JQnm8");
        System.out.println(account.getPublicKey());
        System.out.println(account.getAddress());
        try {
            KeyPair key = CryptoBouncyWrapper.generateGOSTKeyPair();
            System.out.println(Base58.encode(key.getPublic().getEncoded()).length() + ":" + Base58.encode(key.getPublic().getEncoded()));
            System.out.println(Base58.encode(key.getPrivate().getEncoded()).length()  + ":" + Base58.encode(key.getPrivate().getEncoded()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }
}
