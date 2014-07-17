package io.vertigo.rest.engine;

import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.kernel.component.ComponentInfo;
import io.vertigo.kernel.lang.JsonExclude;
import io.vertigo.kernel.lang.Option;
import io.vertigo.kernel.metamodel.DefinitionReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * @author pchretien, npiedeloup
 */
public final class GoogleJsonEngine implements JsonEngine {
	private static final String TOKEN_ID_FIELDNAME = "x-access-token";
	private final Gson gson = createGson();

	/** {@inheritDoc} */
	@Override
	public String toJson(final Object data) {
		return gson.toJson(data);
	}

	/** {@inheritDoc} */
	@Override
	public String toJson(final Object data, final List<String> excludedFields) {
		final JsonElement jsonElement = gson.toJsonTree(data);
		excludeFields(jsonElement, excludedFields);
		return gson.toJson(jsonElement);
	}

	/** {@inheritDoc} */
	@Override
	public String toJsonError(final Throwable th) {
		return "{ globalErrorMessages:[\"" + th.getMessage() + "\"]}"; //TODO +stack;
	}

	private void excludeFields(final JsonElement jsonElement, final List<String> excludedFields) {
		if (jsonElement.isJsonObject()) {
			final JsonObject jsonObject = jsonElement.getAsJsonObject();
			for (final String excludedField : excludedFields) {
				jsonObject.remove(excludedField);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public String toJsonWithTokenId(final Object data, final String tokenId, final List<String> excludedFields) {
		final JsonElement jsonElement = gson.toJsonTree(data);
		excludeFields(jsonElement, excludedFields);
		jsonElement.getAsJsonObject().addProperty(TOKEN_ID_FIELDNAME, tokenId);
		return gson.toJson(jsonElement);
	}

	/** {@inheritDoc} */
	@Override
	public <D extends Object> D fromJson(final String json, final Class<D> paramClass) {
		return gson.fromJson(json, paramClass);
	}

	/** {@inheritDoc} */
	@Override
	public <D extends DtObject> UiObject<D> uiObjectFromJson(final String json, final Class<D> paramClass) {
		final Type typeOfDest = new TypeToken<UiObject<D>>() {
		}.getType();
		return gson.fromJson(json, typeOfDest);
	}

	static class UiObjectDeserializer implements JsonDeserializer<UiObject<?>> {

		public UiObject<?> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
			final Type[] typeParameters = ((ParameterizedType) typeOfT).getActualTypeArguments();
			final Type dtoType = typeParameters[0]; // Id has only one parameterized type T
			final UiObject<DtObject> uiObject = new UiObject(dtoType.getClass());
			final JsonObject jsonObject = json.getAsJsonObject();
			final DtObject inputDto = context.deserialize(jsonObject, dtoType.getClass());
			if (jsonObject.has(TOKEN_ID_FIELDNAME)) {
				uiObject.setAccessTokenKey(jsonObject.get(TOKEN_ID_FIELDNAME).getAsString());
			}
			final Set<String> modifiedFields = new HashSet<>();
			for (final Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				final String fieldName = entry.getKey();
				if (!TOKEN_ID_FIELDNAME.equals(fieldName)) {
					modifiedFields.add(fieldName);
				}
			}
			uiObject.setInputObject(inputDto, modifiedFields);
			return uiObject;
		}
	}

	private static Gson createGson() {
		return new GsonBuilder()//
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") //
				.setPrettyPrinting()//
				//.serializeNulls()//On veut voir les null
				.registerTypeAdapter(UiObject.class, new UiObjectDeserializer())//
				.registerTypeAdapter(ComponentInfo.class, new JsonSerializer<ComponentInfo>() {
					@Override
					public JsonElement serialize(final ComponentInfo componentInfo, final Type typeOfSrc, final JsonSerializationContext context) {
						final JsonObject jsonObject = new JsonObject();
						jsonObject.add(componentInfo.getTitle(), context.serialize(componentInfo.getValue()));
						return jsonObject;
					}
				})//	
				.registerTypeAdapter(List.class, new JsonSerializer<List>() {

					@Override
					public JsonElement serialize(final List src, final Type typeOfSrc, final JsonSerializationContext context) {
						if (src.isEmpty()) {
							return null;
						}
						return context.serialize(src);
					}
				})//	
				.registerTypeAdapter(Map.class, new JsonSerializer<Map>() {

					@Override
					public JsonElement serialize(final Map src, final Type typeOfSrc, final JsonSerializationContext context) {
						if (src.isEmpty()) {
							return null;
						}
						return context.serialize(src);
					}
				})//
				.registerTypeAdapter(DefinitionReference.class, new JsonSerializer<DefinitionReference>() {

					@Override
					public JsonElement serialize(final DefinitionReference src, final Type typeOfSrc, final JsonSerializationContext context) {
						return context.serialize(src.get().getName());
					}
				})//
				.registerTypeAdapter(Option.class, new JsonSerializer<Option>() {

					@Override
					public JsonElement serialize(final Option src, final Type typeOfSrc, final JsonSerializationContext context) {
						if (src.isDefined()) {
							return context.serialize(src.get());
						}
						return null; //rien
					}
				})//			
				.registerTypeAdapter(Class.class, new JsonSerializer<Class>() {

					@Override
					public JsonElement serialize(final Class src, final Type typeOfSrc, final JsonSerializationContext context) {
						return new JsonPrimitive(src.getName());
					}
				})//
				.addSerializationExclusionStrategy(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(final FieldAttributes arg0) {
						if (arg0.getAnnotation(JsonExclude.class) != null) {
							return true;
						}
						return false;
					}

					@Override
					public boolean shouldSkipClass(final Class<?> arg0) {
						return false;
					}
				}).create();
	}

}