package tests;

import endpoints.OrderStorePetEndpoint;
import io.restassured.response.Response;
import models.Order;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import static config.Config.CREATE_ORDER;
import static config.Config.CREATE_PET;
import static config.Config.PETSTORE_BASE_URL;
import static config.Config.STORE_INVENTORY;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

@RunWith(SerenityRunner.class)
public class MyHomeWork {

    @Steps
    public OrderStorePetEndpoint orderStorePetEndpoint;

    @Test
    @Title("Verification status code")
    public void getStatusStoreInventory(){
        orderStorePetEndpoint.getStoreInventory()
                .then()
                .statusCode(200);
    }

    @Test
    @Title("Verification new order can be created")
    public void verifyNewOrderCanBeCreated(){
        Order newOrder = new Order();
        orderStorePetEndpoint.postStoreOrder(newOrder)
                .then()
                .statusCode(200);
    }

    @Test
    @Title("Verification search order by Id")
    public void postOrderForPet(){
        //Given
        Order order = new Order();
        Order.createOrder();
        Response orderResponse =orderStorePetEndpoint.getStoreOrderByOrderId(order.getId());
        Assertions.assertThat(orderResponse.getStatusCode()).isEqualTo(200);
        //When
        Response orderById = orderStorePetEndpoint.getStoreOrderByOrderId(order.getId());
        //Then
        Assertions.assertThat(orderById.getStatusCode()).isEqualTo(200);
    }

    @Test
    @Title("Verification order can be deleted")
    public void deleteOrderByOrderId(){
        //Given
        Order order =new Order();
        Order.createOrder();
        orderStorePetEndpoint.postStoreOrder(order);
        //When
        orderStorePetEndpoint.deleteStoreOrderByOrderId(order.getId());
        Response orderById = orderStorePetEndpoint.getStoreOrderByOrderId(order.getId());
        //Then
        Assertions.assertThat(orderById.getStatusCode()).isEqualTo(404);
    }

    @Test
    @Title("Verification generation API errors with negative values ID")
    public void negativeValueOfIdReturn400(){
        //Given
        Order order = new Order();
        Order.createOrder();
        order.setId(-1);
        orderStorePetEndpoint.postStoreOrder(order);
        //When
        Response orderById = orderStorePetEndpoint.deleteStoreOrderByOrderId(order.getId());
        //Then
        Assertions.assertThat(orderById.getStatusCode()).isEqualTo(400);

    }

    @Test
    @Title("Verification generation exceptions with finding purchase order by ID >= 1 and <= 10")
    public void verifyValueIDByFinding(){
        //Given
        Order order = new Order();
        Order.createOrder();
        order.setId(-1); // {0; 11}
        //When
        Response finding = orderStorePetEndpoint.getStoreOrderByOrderId(order.getId());
        //Then
        Assertions.assertThat(finding.getStatusCode()).isEqualTo(400);
    }


}
