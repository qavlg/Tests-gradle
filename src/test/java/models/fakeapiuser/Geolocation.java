package models.fakeapiuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Geolocation{

	@JsonProperty("lat")
	private Object lat;

	@JsonProperty("long")
	private Object jsonMemberLong;

	public Object getLat(){
		return lat;
	}

	public Object getJsonMemberLong(){
		return jsonMemberLong;
	}
}