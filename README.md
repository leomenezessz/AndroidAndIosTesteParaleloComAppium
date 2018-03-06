# Estudo de testes em paralelo com Appium, Selenium grid e Java.

Estive estudando um pouco sobre como rodar testes em paralelo e a minha maior curiosidade era como poderia executar testes em paralelo para diferentes tipos de plataforma. Para isso tive que entender e estudar o funcionamento de algumas ferramentas antes. Nesta série quero compartilhar minha experiencia com voces de como eu fiz isso, e fazer um tutorial um pouco mais mastigado pra voces ;).

## Ferramentas necessárias para configuração inicial dos testes.

* **Eclipse ou IDE de preferencia**
* **Java 8**

## Let's Go!

### Configuração Inicial

Vamos primeiramente criar o nosso projeto de testes, abra sua IDE, e crie um projeto maven (no meu caso vou usar o eclipse):

```File > New > Maven > Maven Project (Eclipse)```

![alt text](/Users/leonardomenezes/eclipse-workspace/crossTests/imagens/Projeto-Maven-Eclipse.png "Novo Projeto Maven")

Escolha **maven-archetype-quickstart 1.1**, para gerar um projeto mais o simples possível.

![alt text](/Users/leonardomenezes/eclipse-workspace/crossTests/imagens/Arquitetura-Projeto.png "Tipo Arquitetura")

Perfeito, agora vamos no nosso arquivo **pom.xml** e vamos adicionar todas as dependências e plugins que vamos usar no nosso projeto.

* **Java-Client Appium**

```Xml
<dependency>
  <groupId>io.appium</groupId>
  <artifactId>java-client</artifactId>
  <version>4.1.2</version>
</dependency>
```

* **Surefire Maven Plugin**

```Xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>2.19.1</version>
      <configuration>
      <parallel>classes</parallel> <!-- Tag para rodar as classes em paralelo -->
      <threadCount>2</threadCount> <!-- Números de Threads para executar os testes -->
      </configuration>
    </plugin>
  </plugins>
</build>
 ```

Lembrando que no nosso projeto já vem setado o Junit, vamos somente alterar a versão dele para p 4.7.

* **Junit 4**

```Xml
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.7</version>
  <scope>test</scope>
  </dependency>
</dependencies>
 ```

Depois da configuração do **pom.xml**, vamos criar um diretório que vamos colocar os nossos apps.

```File > New > Folder > [folder-name]```

![alt text](/Users/leonardomenezes/eclipse-workspace/crossTests/imagens/Nova-Pasta.png "Tela Criação de Pastas")

Vou colocar aqui também os arquivos dos app para donwload:

* [APK-ANDROID](https://www.google.com)
* [APP-IOS](https://www.google.com)

Faço o donwload deles e jogue na pasta que vc acabou de criar!

## Criação das Classes

Ufa, depois de todas essas configuraçoes podemos começar a criar nossas clases, a idéa seria utilizar o padrão Page Object, nos nossos testes. Vamos também testar aplicações semelhantes onde a aplicação IOS tem as mesmas funcionalidades que a aplicação android! Dentro da nossa estrutura **src/test/java/meu-pacote-de-tests** já existe uma classe de teste que foi criado junto com nossa estrutura pode excluir ela!

 Vamos criar um outro pacote que ficará todas as nossas screens.

 ```File > New > Package > [package-name]```

![alt text](/Users/leonardomenezes/eclipse-workspace/crossTests/imagens/Package-Screen.png "Tela Criação de Pastas")

E dentro deste pacote vamos criar a classe screen que vai contemplar todos elementos da tela da nossa aplicação.

```Java
public class CalculatorScreen {

private AppiumDriver<MobileElement> driver;

public CalculatorScreen(AppiumDriver<MobileElement> driver)  {
	PageFactory.initElements( new AppiumFieldDecorator(driver), this);
	this.driver = driver;
  }

}
```

Esse é o inicio da nossa classe e a parte mais importante rsrsrs, o PageFactory responsavél por buscar todos os meus elementos em tela, para isso precisamos de uma instancia do nosso driver e do contexto da page. Criamos o PageFactory no meu construtor porque sempre criar uma instancia da minha CalculatorScreen ele vai buscar todos os seu elementos com o driver especifico que recebermos no construtor. Agora vamos pegar os elementos da tela, não vou mostrar como fazer isso aqui e nem como configurar os emuladores mais posso ajudar com os links, existem diversas ferramentas para isso!

### Inspetores de tela

* [MacacaJs](https://medium.com/@deyvirsonmendona/inspecionando-elementos-no-app-ios-com-macacajs-cad962719ce2)
* [UIAutomatorviewer](https://www.guru99.com/uiautomatorviewer-tutorial.html)
* [Arc](https://github.com/appium/ruby_console)

E depois de pegar os elementos podemos atualizar nossa page, e agora temos esse resultado :

```Java
public class CalculatorScreen {

private AppiumDriver<MobileElement> driver;

public CalculatorScreen(AppiumDriver<MobileElement> driver)  {
	PageFactory.initElements( new AppiumFieldDecorator(driver), this);
	this.driver = driver;
}

  @AndroidFindBy(id = "android_button_sum")
	@iOSFindBy(accessibility = "apple-sum-button")
	public RemoteWebElement buttonSum;

  @AndroidFindBy(id = "android_result_text")
	@iOSFindBy(accessibility = "apple_result_text")
	private RemoteWebElement resultText;

  @AndroidFindBy(id = "android_field_first_number")
	@iOSFindBy(accessibility = "apple_first_input")
	private RemoteWebElement inputFirstNumber;

	@AndroidFindBy(id = "android_field_second_number")
	@iOSFindBy(accessibility = "apple_second_input")
	private RemoteWebElement inputSecondNumber;
}```  


Reparem nas annotations, dentro delas especificamos qual será o nosso locator, temos os locators separados tanto do android como o do ios em apenas uma variavel! Visivelmente não temos aquele findElement(By())... Consegue visualizar a abstração da nossa page? Agora vamos finalizar nossa classe criando os métodos com as ações da page.

```Java
	public CalculatorScreen fillFirstNumber(String number) {
		inputFirstNumber.click();
		inputFirstNumber.clear();
		driver.getKeyboard().sendKeys(number);
		return this;
	}

	public CalculatorScreen fillSecondNumber(String number) {
		inputSecondNumber.click();
		inputSecondNumber.clear();
		driver.getKeyboard().sendKeys(number);
		return this;
	}

	public CalculatorScreen closeKeyboard() {
		driver.getKeyboard().pressKey(Keys.RETURN);
		return this;
	}

	public String operationResult() {
		return resultText.getText().toString().trim();
	}

	public void quitDriver() {
		driver.quit();
	}
  ```
  
  Beleza! Vamos criar as duas classes de teste para android e ios, dentro do pacote **src/test/java/meu-pacote-de-tests**

  * **Classe de Teste Android**

  ```Java
  public class TestAndroidCalculator {

	private static CalculatorScreen calculatorScreen;
	private static AppiumDriver<MobileElement> appiumDriver;

	@BeforeClass
	public static void setup() throws MalformedURLException {
	  DesiredCapabilities capabilities = new DesiredCapabilities();
	  capabilities.setCapability("app",new File("apps/app-android-calculator.apk"));
	  capabilities.setCapability("platformName","Android" );
	  capabilities.setCapability("deviceName","Android Emulator API 22");
	  capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("disableAndroidWatchers" , true);
	  appiumDriver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub") , capabilities);
	  calculatorScreen = new CalculatorScreen(appiumDriver);
	}

	@Test
	public void shouldSum() {
		calculatorScreen.fillFirstNumber("10").fillSecondNumber("10").buttonSum.click();
		assertTrue(calculatorScreen.operationResult().equals("20"));
	}

	@AfterClass
	public static void teardown() {
		calculatorScreen.quitDriver();
	}
}
```

* **Classe de Teste IOS**

```Java
public class TestIosCalculator {

	private static CalculatorScreen calculatorScreen;
	private static AppiumDriver<MobileElement> appiumDriver;

	@BeforeClass
	public static void setup() throws MalformedURLException {
	  DesiredCapabilities capabilities = new DesiredCapabilities();
	  capabilities.setCapability("app",new File("apps/SimpleCalculator.app"));
	  capabilities.setCapability("plataform", "MAC" );
	  capabilities.setCapability("plataformName", "ios" );
	  capabilities.setCapability("deviceName", "iPhone SE");
	 	capabilities.setCapability("automationName" , "XCUITest");
		appiumDriver = new IOSDriver<MobileElement>(new URL("http://localhost:4723/wd/hub") , capabilities);
		calculatorScreen = new CalculatorScreen(appiumDriver);
	}

	@Test
	public void should_sum() {
		calculatorScreen.fillFirstNumber("10").fillSecondNumber("10").closeKeyboard().buttonSum.click();
		assertTrue(calculatorScreen.operationResult().equals("20"));
	}

	@AfterClass
	public static void teardown() {
		calculatorScreen.quitDriver();
	}
}
```

Os casos de teste são bem simples, estão apenas verificando se a aplicação está realizando corretamente o fluxo da soma. Especifiquei também o capabilities de cada dispositivo, e vou deixar um link aqui explicando o
que faz cada capabilitie.

Show, agora já podemos rodar os testes. Inicie seu emulador android (não precisa iniciar o simulador ios) e inicie o appium como de costume com o comando

```Console
$ appium
```

Prontinho agora só rodar os testes, no meu caso rodei direto com o junit clicando com o direito na classe da executando um **Run as > Junit Test**.

![alt text](/Users/leonardomenezes/eclipse-workspace/crossTests/imagens/Junit-Run.png "Execução Junit")

No proximo post vou focar no selenium grid e como vamos fazer nossa estrutura atual rodar em paralelo no selenium grid.

Obrigado <3


### Refrencias

* **Repositorio deste Projeto :** https://github.com/menezes-ssz/
* **Appium capabilities :** https://appium.io/docs/en/writing-running-appium/caps/
* **Git Hub :** https://github.com/menezes-ssz/














  
