package org.spectraLogic.systemMessagesTracker.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.spectraLogic.systemMessagesTracker.commandLine.CommandLineOptions;

public class HttpClient {
	private SSLSocketFactory socketFactory;
	private final int READ_TIMEOUT_MILLIS = 60 * 1000;

	public InputStream getMessages(CommandLineOptions options) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException,
	CertificateException, IOException, URISyntaxException {

		login(options.ipAddress, options.username, options.password, options.isSsl);
		return getInputStreamFromUrl(options.ipAddress, "systemMessages.xml", options.isSsl);

	}

	public void login(String ip, String username, String password, boolean isSsl) throws KeyStoreException, IOException, URISyntaxException,
	KeyManagementException, NoSuchAlgorithmException, CertificateException {
		if(isSsl)	{
			SslSocketFactoryBuilder builder = new SslSocketFactoryBuilder();
			socketFactory = builder.getSslSocketFactory();
		}

		manageCookies();

		String loginString = "login.xml?username=" + username + "&password=" + password;

		InputStream loginStream = getInputStreamFromUrl( ip, loginString, isSsl);
		String resutls = IOUtils.toString(loginStream);


		if(!resutls.toLowerCase().contains("<status>ok</status")){
			throw new IOException("Bad Login: " + resutls);
		}
	}

	private void manageCookies() {

		if(CookieHandler.getDefault() == null){
			CookieManager cookieManager = new CookieManager();
			cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(cookieManager);
		}
	}

	protected InputStream getInputStreamFromUrl(String ip, String urlAddress, boolean isSsl) throws IOException, URISyntaxException, KeyStoreException,
	KeyManagementException, NoSuchAlgorithmException, CertificateException {

		if(isSsl) {
			if(socketFactory == null){
				SslSocketFactoryBuilder builder = new SslSocketFactoryBuilder();
				socketFactory = builder.getSslSocketFactory();
			}
			urlAddress = "https://" + ip + "/gf/" + urlAddress;


			URL url = createUrlFromString(urlAddress);

			HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
			httpsCon.setSSLSocketFactory(socketFactory);
			httpsCon.setReadTimeout(READ_TIMEOUT_MILLIS);
			httpsCon.setHostnameVerifier(new HostnameVerifier()
			{
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			httpsCon.connect();
			return httpsCon.getInputStream();
		}
		String realUrlAddress = "http://" + ip + "/gf/" + urlAddress;


		URL url = createUrlFromString(realUrlAddress);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setReadTimeout(READ_TIMEOUT_MILLIS);
		connection.connect();

		try{
			return connection.getInputStream();

		} catch (IOException e){
			if(connection.getResponseCode() / 100 != 2){

				return connection.getErrorStream();
			} else {
				throw e;
			}
		} catch (IllegalArgumentException e){
			throw new IOException("Caught IllegalArgumentException with message " + e.getMessage());
		}
	}

	private URL createUrlFromString(String verifiedCommand) throws URISyntaxException, MalformedURLException {
		URI uri = new URI(verifiedCommand);
		URL url = uri.toURL();
		return url;
	}

	public String getSerialNumber(CommandLineOptions options) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, URISyntaxException {

		//login(options.ipAddress, options.username, options.password, options.isSsl);

		InputStream stream = getInputStreamFromUrl(options.ipAddress, "libraryStatus.xml", options.isSsl);

		String xml = IOUtils.toString(stream);

		if(xml.contains("<serialNumber>")){
			String sn = xml.split("<serialNumber>")[1];
			sn = sn.split("</serialNumber>")[0];
			return sn;
		} else {

			return options.ipAddress;
		}
	}
}
