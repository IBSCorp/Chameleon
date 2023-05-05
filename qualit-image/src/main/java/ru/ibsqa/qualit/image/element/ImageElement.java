package ru.ibsqa.qualit.image.element;

import ru.ibsqa.qualit.elements.selenium.IFacadeSelenium;
import ru.ibsqa.qualit.image.driver.ImageDriver;
import ru.ibsqa.qualit.page_factory.locator.ISearchStrategy;
import ru.ibsqa.qualit.reporter.IReporterManager;
import ru.ibsqa.qualit.selenium.driver.IDriverManager;
import ru.ibsqa.qualit.selenium.driver.WebDriverFacade;
import ru.ibsqa.qualit.selenium.enums.KeyEnum;
import ru.ibsqa.qualit.utils.delay.DelayUtils;
import ru.ibsqa.qualit.utils.spring.SpringUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class ImageElement implements IFacadeSelenium {

	private int x1;
	private int x2;
	private int y1;
	private int y2;
	private double max_val;

	private final WebDriverFacade driver;

	private IReporterManager reporterManager = SpringUtils.getBean(IReporterManager.class);

	public ImageElement(String subImage, WebDriverFacade driver) {
		this.driver = driver;
		long start1 = System.currentTimeMillis();
		InputStream screen = createActualImage(subImage);
		log.info("сняли скрин" + (System.currentTimeMillis() - start1));
		long maxWaitTime = System.currentTimeMillis() + driver.getImplicitlyWait() * 1000;
		do {
			log.info("Поиск -  " + subImage);
			long start = System.currentTimeMillis();
			String response = RestAssured.given()
                    .multiPart("image", System.currentTimeMillis() + ".png", screen)
                    .multiPart("sub_image", new File(subImage.split(";")[0]))
                    .post(getDriver().getConfiguration().getDriverPath()).prettyPrint();
		//	log.info("конец запроса" + (System.currentTimeMillis() - start));
			if (((ArrayList) JsonPath.from(response).get()).size() > 0) {
				this.x1 = JsonPath.from(response).get("pos_x1[0]");
				this.x2 = JsonPath.from(response).get("pos_x2[0]");
				this.y1 = JsonPath.from(response).get("pos_y1[0]");
				this.y2 = JsonPath.from(response).get("pos_y2[0]");
				this.max_val = Double.parseDouble(JsonPath.from(response).get("max_val[0]").toString());
				if (this.max_val >= 0.75) {
					return;
				}
			}

		} while (System.currentTimeMillis() < maxWaitTime);
		throw new AssertionError("Не найден элемент: " + subImage);
	}

	private InputStream createActualImage(String subImage) {
		WebDriverFacade defaultDriver = SpringUtils.getBean(IDriverManager.class).getDrivers().stream().filter(WebDriverFacade::isDefaultDriver).findAny().orElse(null);
		InputStream screen = null;
		if (defaultDriver!=null && defaultDriver.getWrappedDriver().getClass().getSuperclass().isAssignableFrom(RemoteWebDriver.class)){
			if (subImage.contains(";")){
				screen = takeWebElementSimpleScreenshot(subImage, defaultDriver);
			}else {
				screen = takeWebDriverSimpleScreenshot(defaultDriver);
			}

		} else {
			screen = getFullScreenshot();
		}
		try {
			reporterManager.createAttachment("Ожидаемое изображение", new FileInputStream(new File(subImage.split(";")[0])), null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return screen;
	}


	public Robot getRobot() {
		return getImageDriver().getRobot();
	}

	public ImageDriver getImageDriver() {
		return ((ImageDriver) getDriver());
	}

	@Override
	public void moveMouseTo() {
		getRobot().mouseMove((x2 - x1) / 2 + x1, (y2 - y1) / 2 + y1);
	}

	@Override
	public void click() {
		moveMouseTo();
		getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
		getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	@Override
	public void doubleClick() {
		moveMouseTo();
		getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
		getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		getRobot().delay(200);
		getRobot().mousePress(InputEvent.BUTTON1_DOWN_MASK);
		getRobot().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	@Override
	public void rightClick() {
		moveMouseTo();
		getImageDriver().getRobot().delay(200);
		getRobot().mousePress(InputEvent.BUTTON3_DOWN_MASK);
		getRobot().mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	}

	@Override
	public void submit() {
		fail("Image.Element.submit() не реализован");
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		StringSelection stringSelection = new StringSelection(keysToSend[0].toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);
		click();
		getRobot().keyPress(KeyEvent.VK_CONTROL);
		getRobot().keyPress(KeyEvent.VK_V);
		getRobot().keyRelease(KeyEvent.VK_V);
		getRobot().keyRelease(KeyEvent.VK_CONTROL);
		DelayUtils.sleep(1000);
		getRobot().keyPress(KeyEvent.VK_TAB);
		getRobot().keyRelease(KeyEvent.VK_TAB);
	}

	@Override
	public void clear() {
		click();
		getRobot().keyPress(KeyEvent.VK_CONTROL);
		getRobot().keyPress(KeyEvent.VK_A);
		getRobot().keyRelease(KeyEvent.VK_A);
		getRobot().keyRelease(KeyEvent.VK_CONTROL);
		getRobot().keyPress(KeyEvent.VK_DELETE);
		getRobot().keyRelease(KeyEvent.VK_DELETE);

	}

	@Override
	public String getTagName() {
		fail("Image.Element.getTagName() не реализован");
		return null;
	}

	@Override
	public String getAttribute(String name) {
		fail("Image.Element.getAttribute() не реализован");
		return null;
	}

	@Override
	public boolean isSelected() {
		fail("Image.Element.isSelected() не реализован");
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getText() {
		fail("getText() не реализован");
		return null;
	}

	@Override
	public List<WebElement> findElements(By by) {
		fail("findElements() не реализован");
		return null;
	}

	@Override
	public WebElement findElement(By by) {
		if (by.toString().equals("By.xpath: .")) {
			return this;
		}

		return null;
	}

	@Override
	public boolean isDisplayed() {
		return true;
	}

	@Override
	public Point getLocation() {
		Rectangle rect = getRect();
		return new Point(rect.getX(), rect.getY());
	}

	@Override
	public Dimension getSize() {
		Rectangle rect = getRect();
		return new Dimension(rect.getWidth(), rect.getHeight());
	}

	@Override
	public Rectangle getRect() {
		return null;
	}


	@Override
	public String getCssValue(String propertyName) {
		return null;
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return null;
	}

	@Override
	public String getPlaceholder() {
		return null;
	}

	@Override
	public void pressKey(KeyEnum key) {
		getRobot().keyPress(key.getValue());
		getRobot().keyRelease(key.getValue());
	}

	@Override
	public void type(String value) {
		sendKeys(value);
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public String getErrorMsg() {
		return null;
	}

	@Override
	public String getLabel() {
		return null;
	}

	@Override
	public WebDriverFacade getDriver() {
		return driver;
	}

	@Override
	public WebElement getWrappedElement() {
		return this;
	}

	/**
	 * Скриншот экрана
	 *
	 * @return
	 */
	private InputStream getFullScreenshot() {
		try {
			java.awt.Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
			java.awt.Rectangle rectangle = new java.awt.Rectangle(resolution);
			Robot robot = new Robot();
			BufferedImage bufferedImage = robot.createScreenCapture(rectangle);

			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				ImageIO.write(bufferedImage, "png", os);
				try {
					reporterManager.createAttachment("Актуальное изображение", new ByteArrayInputStream(os.toByteArray()), null, null);
					return new ByteArrayInputStream(os.toByteArray());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private InputStream takeWebDriverSimpleScreenshot(WebDriverFacade webDriver) {
		byte[] bytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
		try {
			reporterManager.createAttachment("Актуальное изображение", new ByteArrayInputStream(bytes), null, null);
			return new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}


	private InputStream takeWebElementSimpleScreenshot(String path, WebDriverFacade webDriver) {
		By by = SpringUtils.getBean(ISearchStrategy.class).getLocator(path.split(";")[1]);
		WebElement element = webDriver.findElement(by);
		JavascriptExecutor js = (JavascriptExecutor) webDriver;

		js.executeScript(
				"var headID = document.getElementsByTagName('head')[0];" +
						"var newScript = document.createElement('script');" +
						"newScript.type = 'text/javascript';" +
						"newScript.src = 'https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js';" +
						"headID.appendChild(newScript);");
		WebDriverWait waitJQ = new WebDriverWait(webDriver, Duration.ofSeconds(30));
		Function<WebDriver, Boolean> jQueryAvailable = WebDriver -> (
				(Boolean) js.executeScript("return (typeof jQuery != \"undefined\")")
		);
		waitJQ.until(jQueryAvailable);
		Screenshot screenshot = new AShot().takeScreenshot(webDriver, element);
		if (null != screenshot) {
			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				ImageIO.write(screenshot.getImage(), "png", os);
				try {
					reporterManager.createAttachment("Актуальное изображение", new ByteArrayInputStream(os.toByteArray()), null, null);
					return new ByteArrayInputStream(os.toByteArray());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;

	}

	@Override
	public Coordinates getCoordinates() {
		return null;
	}
}
