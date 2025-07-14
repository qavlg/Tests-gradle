package models.fakeapiproduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product{

	@JsonProperty("image")
	private String image;

	@JsonProperty("color")
	private String color;

	@JsonProperty("price")
	private String price;

	@JsonProperty("description")
	private String description;

	@JsonProperty("discount")
	private String discount;

	@JsonProperty("model")
	private String model;

	@JsonProperty("id")
	private int id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("category")
	private String category;

	@JsonProperty("brand")
	private String brand;

	@JsonProperty("popular")
	private boolean popular;
}