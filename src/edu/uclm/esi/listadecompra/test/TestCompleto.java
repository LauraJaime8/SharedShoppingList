package edu.uclm.esi.listadecompra.test;

import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mysql.jdbc.Connection;

import edu.uclm.esi.listadecompra.dao.BrokerPool;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestCompleto {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	  System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	    
		driver = new ChromeDriver();
	    baseUrl = "http://localhost:8080/";

	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }
  
  @Before
  public void vaciarUsuarios() throws Exception{
	  java.sql.Connection bd=BrokerPool.get().getConnectionSeleccion();
	  String sql="Delete from Usuario";
	  PreparedStatement ps = bd.prepareStatement(sql);
	  ps.executeUpdate();
	  bd.close();
  }
  
 

  @Test
  public void testRegistrarFlujoNormal() throws Exception {
	  try{
		  driver.get(baseUrl + "/listaDeCompra/");
		  driver.findElement(By.id("btnRegistrar")).click();
		  driver.findElement(By.id("cajaEmailRegistro")).clear();
		  driver.findElement(By.id("cajaEmailRegistro")).sendKeys("pepito@pepito.com");
		  driver.findElement(By.id("cajaPwd1")).clear();
		  driver.findElement(By.id("cajaPwd1")).sendKeys("pepito");
		  driver.findElement(By.id("cajaPwd2")).clear();
		  driver.findElement(By.id("cajaPwd2")).sendKeys("pepito");
		  driver.findElement(By.xpath("(//button[@id='btnRegistrar'])[2]")).click();
		  assertEquals("Se ha registrado correctamente", closeAlertAndGetItsText());
		  assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
		  fail("Esperaba NoAlertPresentException");
	  }catch(NoAlertPresentException e){
	  }
  }


  @Test
  public void testPassDiferentes() throws Exception {
	  try{
		  driver.get(baseUrl + "/listaDeCompra/");
		  driver.findElement(By.id("btnRegistrar")).click();
		  driver.findElement(By.id("cajaEmailRegistro")).clear();
		  driver.findElement(By.id("cajaEmailRegistro")).sendKeys("lolo@lolo.com");
    	driver.findElement(By.id("cajaPwd1")).clear();
    	driver.findElement(By.id("cajaPwd1")).sendKeys("lolo");
    	driver.findElement(By.id("cajaPwd2")).clear();
    	driver.findElement(By.id("cajaPwd2")).sendKeys("pepe");
    	driver.findElement(By.xpath("(//button[@id='btnRegistrar'])[2]")).click();
    	assertEquals("Error: Las passwords no coinciden", closeAlertAndGetItsText());
    	  //fail("Esperaba NoAlertPresentException");
	  }catch(NoAlertPresentException e){
	  }
  }

  @Test
  public void testPassCorta() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    driver.findElement(By.id("btnRegistrar")).click();
    driver.findElement(By.id("cajaEmailRegistro")).clear();
    driver.findElement(By.id("cajaEmailRegistro")).sendKeys("laura@gmail.com");
    driver.findElement(By.id("cajaPwd1")).clear();
    driver.findElement(By.id("cajaPwd1")).sendKeys("lau");
    driver.findElement(By.id("cajaPwd2")).clear();
    driver.findElement(By.id("cajaPwd2")).sendKeys("lau");
    driver.findElement(By.xpath("(//button[@id='btnRegistrar'])[2]")).click();
    assertEquals("Error: Tu contraseña es muy corta", closeAlertAndGetItsText());
	  }catch(NoAlertPresentException e){
	  }
  }
  
  @Test
  public void testErrorPass() throws Exception {
    driver.get(baseUrl + "/listaDeCompra/");
    driver.findElement(By.id("cajaEmailLogin")).clear();
    driver.findElement(By.id("cajaEmailLogin")).sendKeys("pepe@pepe.com");
    driver.findElement(By.id("cajaPwd")).clear();
    driver.findElement(By.id("cajaPwd")).sendKeys("laura");
    driver.findElement(By.id("btnLogin")).click();
    assertEquals("Error: Error en tu email o password. Quiza no estes registrado.", closeAlertAndGetItsText());
  }
  
  @Test
  public void testLoguinFlujoNormal() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    driver.findElement(By.id("cajaEmailLogin")).clear();
    driver.findElement(By.id("cajaEmailLogin")).sendKeys("pepe@pepe.com");
    driver.findElement(By.id("cajaPwd")).clear();
    driver.findElement(By.id("cajaPwd")).sendKeys("pepe");
    driver.findElement(By.id("btnLogin")).click();
    assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
    
	  }catch(Exception e){
	  }
  }
  
 

  @Test
  public void testConectarseFlujoNormal() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
	  }catch(NoAlertPresentException e){
	  }
  }


  @Test
  public void testSeleccionarLista() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
    driver.findElement(By.linkText("Lista Prueba")).click();
	  }catch(NoAlertPresentException e){
	  }
  }



  @Test
  public void testCrearListaFlujoNormal() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    assertEquals("Se ha conectado con el usuario: lolo@lolo.com", closeAlertAndGetItsText());
    driver.findElement(By.id("cajaNuevaLista")).clear();
    driver.findElement(By.id("cajaNuevaLista")).sendKeys("Navidad");
    driver.findElement(By.id("btnNuevaLista")).click();
    assertEquals("Se ha creado la lista Navidad", closeAlertAndGetItsText());
	  }catch(NoAlertPresentException e){
	  }
  }

  @Test
  public void testListaExiste() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
    driver.findElement(By.id("btnNuevaLista")).click();
    assertEquals("La lista Piso ya existe", closeAlertAndGetItsText());
	  }catch(NoAlertPresentException e){
	  }
  }

 
  @Test
  public void testInvitarFlujoNormal() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    assertEquals("Se ha conectado con el usuario: pepe@pepe.com", closeAlertAndGetItsText());
    driver.findElement(By.linkText("Piso")).click();
    driver.findElement(By.id("emailInvitado")).clear();
    driver.findElement(By.id("emailInvitado")).sendKeys("laura@laura.com");
    driver.findElement(By.id("btnInvitar")).click();
    assertEquals("Se ha invitado a laura@laura.com", closeAlertAndGetItsText());
	  }catch(NoAlertPresentException e){
	  }
  }

  @Test
  public void testUnirseFlujoNormal() throws Exception {
	  try{
   /* driver.get(baseUrl + "/mail/#inbox");
    driver.findElement(By.id(":bg")).click();
    driver.findElement(By.cssSelector("span[name=\"practica.listacompra@gmail.com\"]")).click();*/
    driver.findElement(By.linkText("http://localhost:8080/listaDeCompra/unirse.jsp?p=1430868688")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [waitForPopUp | _blank | 30000]]
    assertEquals("Se ha conectado con el usuario: lolo@lolo.com", closeAlertAndGetItsText());
    driver.findElement(By.linkText("Piso")).click();
	  }catch(NoSuchElementException e){
	  }
  }

 
  @Test
  public void testAddProd() throws Exception {
	  try{
		  
	 
    driver.get(baseUrl + "/listaDeCompra/");
    driver.findElement(By.id("nombreProducto")).clear();
    driver.findElement(By.id("nombreProducto")).sendKeys("Leche");
    driver.findElement(By.id("cantidadDeseada")).clear();
    driver.findElement(By.id("cantidadDeseada")).sendKeys("2");
    driver.findElement(By.id("cantidadExistente")).clear();
    driver.findElement(By.id("cantidadExistente")).sendKeys("1");
    driver.findElement(By.id("btnAnadirProducto")).click();
	  }catch(InvalidElementStateException e){
	  }
  }
  
  @Test
  public void testComprar() throws Exception {
	  try{
    driver.get(baseUrl + "/listaDeCompra/");
    driver.findElement(By.xpath("//button[@onclick='lista.comprar(29)']")).click();
	  }catch(NoSuchElementException e){
	  }
  }
  
  

  @Test
  public void testModificarProducto() throws Exception {
	  try{
		  driver.get(baseUrl + "/listaDeCompra/");
		  driver.findElement(By.linkText("Leche")).click();
		  driver.findElement(By.id("cantidadDeseada")).clear();
		  driver.findElement(By.id("cantidadDeseada")).sendKeys("3");
		  driver.findElement(By.id("btnEditarProducto")).click();
		  assertTrue(By.id("cantidadDeseada").equals(3));

	  }catch(NoSuchElementException e){ 
	  }
  }

  
  
  
  
  
  
  
  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
