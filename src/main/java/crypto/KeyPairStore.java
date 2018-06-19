package crypto;

import org.bouncycastle.jce.spec.ECPrivateKeySpec;

import java.io.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class KeyPairStore {
    private KeyPair keyPair;
    private String storePassword;
    private String aliasName;

    KeyPairStore(KeyPair keyPair, String aliasName, String storePassword) {
        setKey(keyPair);
        this.aliasName = aliasName;
        this.storePassword = storePassword;
        //File keyStoreFile = new File(aliasName + ".key");
    }

    public void setKey(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public boolean save() throws IOException {
        BufferedWriter out = null;
        try {
            File file = new File(aliasName + ".key");
            if (!file.exists() && !file.createNewFile())
                return false;
            out = new BufferedWriter(new FileWriter((file)));
            out.write(Base58.encode(this.keyPair.getPrivate().getEncoded()));
            out.newLine();
            out.write(Base58.encode(this.keyPair.getPublic().getEncoded()));
            out.newLine();
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return true;
    }

    private boolean read() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        BufferedReader in = null;
        try {
            File file = new File(aliasName + ".key");
            if (!file.exists())
                return false;
            in = new BufferedReader(new FileReader((file)));
            String rawPrivateKey = in.readLine();
            String rawPublicKey = in.readLine();
            KeyFactory f = KeyFactory.getInstance("ECGOST3410-2012", "BC");
            //PrivateKey sKey = f.generatePrivate(new ECPrivateKeySpec(rawPrivateKey,new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA")));
            //PublicKey vKey = f.generatePublic(pubKey);
            //this.keyPair = new KeyPair(Base58.decode(rawPublicKey),Base58.decode(rawPrivateKey));
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return true;
    }
}
