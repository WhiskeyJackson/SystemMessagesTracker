package org.spectraLogic.systemMessagesTracker.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SslSocketFactoryBuilder {

	private SSLSocketFactory socketFactory;


	public synchronized SSLSocketFactory getSslSocketFactory() throws KeyStoreException, KeyManagementException, NoSuchAlgorithmException, CertificateException, IOException{

		if( socketFactory == null )
		{
			socketFactory = buildSocketFactory();
		}
		return socketFactory;
	}


	private SSLSocketFactory buildSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException{

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		char[] password = new char[0];
		keystore.load(null, password);
		Certificate cert = getSpectraCertificate();
		keystore.setEntry("SpectraLogicCert", new KeyStore.TrustedCertificateEntry(cert), null);

		TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustFactory.init(keystore);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustFactory.getTrustManagers(), null);

		return sslContext.getSocketFactory();

	}

	private Certificate getSpectraCertificate() throws CertificateException{

		InputStream stream = SslSocketFactoryBuilder.class.getResourceAsStream("SpectraLogicCertificate.cer");
		BufferedInputStream buffStream = new BufferedInputStream(stream);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return cf.generateCertificate(buffStream);

	}

}
