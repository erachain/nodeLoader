package crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
public class CryptoBouncyWrapper {

    public static KeyPair generateGOSTKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECGOST3410-2012", "BC");
        keyGen.initialize(new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
        return keyGen.genKeyPair();
    }

}
