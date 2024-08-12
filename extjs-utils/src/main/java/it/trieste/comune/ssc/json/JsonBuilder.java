package it.trieste.comune.ssc.json;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.servlet.ServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is useful for building gson/json responses for ajax usage,
 * expecially with extjs
 *
 * sample usage is
 * JsonBuilder.newInstance().withSourceData(data).buildStoreResponse(); or
 * JsonBuilder.newInstance().withMessage("hello!").buildResponse();
 *
 * if provided with a Writer, will send output directly to it
 *
 * JsonBuilder.newInstance().withWriter(httpServletResponse.getWriter()).withMessage("hello!").buildResponse();
 *
 * otherwise you should send the response via gson or other json framework
 *
 * Object responseObj =
 * JsonBuilder.newInstance().withMessage("hello!").buildResponse();
 * gson.toJson(responseObj,httpServletResponse.getWriter());
 *
 * other data/functions may be provided for processing/filtering of response
 *
 * JsonBuilder.newInstance().withSourceData(data).withTransformer(new
 * Function(){...}).withSorter(new
 * Comparator(){...}).withLimit(99).buildStoreResponse();
 *
 * limit/start/ordering may be parsed directly from request parameters
 *
 * JsonBuilder.newInstance().withParameters(servletRequest.getParameterMap())
 *
 * all computation is made lazily when build*Response() is called, so you may
 * provide the parameters in any order . .
 *
 * @author aleph
 */
public class JsonBuilder {

    public static final String DEFAULT_RESPONSE = "operazione completata", ERROR_RESPONSE = "si e' verificato un errore";
    public static final Logger logger = LoggerFactory.getLogger(JsonBuilder.class);
    private static final Gson gson = new Gson(), gsonPrettyPrinting = new GsonBuilder().setPrettyPrinting().create();
    private Object data;
    private Map<String, String> mapData;
    private Integer total, limit, start;
    private Function transformer;
    private Predicate filter;
    private Comparator sorter;
    private String message = DEFAULT_RESPONSE;
    private boolean sortEarly = false, success = true, filterEarly = false;
    private Writer writer;

    public static Gson getGson() {
        return gson;
    }

    public static Gson getGsonPrettyPrinting() {
        return gsonPrettyPrinting;
    }

    public static JsonBuilder newInstance() {
        return new JsonBuilder();
    }

    /**
     * will set the data to be incorporated in the response. Used for both
     * generic (non-store) data, and Iterable store data
     *
     * @param data
     * @return
     */
    public JsonBuilder withData(Object data) {
        this.data = data;
        return this;
    }

    public JsonBuilder addRecord(Object record) {
        if (data == null) {
            data = Lists.newArrayList();
        }
        Preconditions.checkArgument(data instanceof Collection);
        ((Collection) data).add(record);
        return this;
    }

    /**
     * Set the provided value, to be returned as attribute in the 'data'
     * attribute of response.
     *
     * @param key
     * @param value
     * @return
     */
    public JsonBuilder withValue(String key, Object value) {
        if (mapData == null) {
            mapData = Maps.newHashMap();
        }
        mapData.put(key, String.valueOf(value));
        return this;
    }

    public JsonBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     *
     * @param sourceData
     * @return
     * @deprecated use withData(object);
     */
    @Deprecated
    public JsonBuilder withSourceData(Iterable sourceData) {
        return withData(sourceData);
    }

    public JsonBuilder withTotal(Integer total) {
        this.total = total;
        return this;
    }

    public JsonBuilder withLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public JsonBuilder withStart(Integer start) {
        this.start = start;
        return this;
    }

    public JsonBuilder withTransformer(Function transformer) {
        this.transformer = transformer;
        return this;
    }

    public JsonBuilder withFilter(Predicate filter) {
        this.filter = filter;
        return this;
    }

    public JsonBuilder withFilter(String filter, String... restrictFields) {
        return withFilter(buildMapFilter(filter, restrictFields));
    }

    public JsonBuilder withSorter(Comparator sorter) {
        this.sorter = sorter;
        return this;
    }

    public JsonBuilder withSorter(Comparator sorter, boolean sortEarly) {
        this.sorter = sorter;
        this.sortEarly = sortEarly;
        return this;
    }

    public JsonBuilder withSorter(String comparatorStr) {
        sorter = buildComparator(comparatorStr);
        return this;
    }

    public JsonBuilder withSorter(final JsonSortInfo... sortInfoList) {
        sorter = buildComparator(sortInfoList);
        return this;
    }

    public JsonBuilder sortEarly(boolean sortEarly) {
        this.sortEarly = sortEarly;
        return this;
    }

    public JsonBuilder sortEarly() {
        return sortEarly(true);
    }

    public JsonBuilder filterEarly(boolean filterEarly) {
        this.filterEarly = filterEarly;
        return this;
    }

    public JsonBuilder filterEarly() {
        return filterEarly(true);
    }

    public JsonBuilder withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * set writer. If writer is set, output will be appent to it
     *
     * @param writer
     * @return
     */
    public JsonBuilder withWriter(Writer writer) {
        this.writer = writer;
        return this;
    }

    public JsonBuilder withError(Throwable error) {
        if (message.equals(DEFAULT_RESPONSE)) {
            while (!error.getClass().equals(IllegalArgumentException.class) && error.getCause() != null) {
                error = error.getCause();
            }
            if (error.getClass().equals(IllegalArgumentException.class)) {
                message = error.getMessage();
            } else {
                message = error.toString();
            }
            message = ERROR_RESPONSE + ": " + message;
        }
        return this.withSuccess(false);
    }

    public JsonBuilder withError(String errorMessage) {
        return this.withSuccess(false).withMessage(message);
    }

    public static Map<String, String> getRequestParameters(ServletRequest request) {
        return requestParametersMapToParametersMap(request.getParameterMap());
    }

    private static Map<String, String> requestParametersMapToParametersMap(Map params) {
        Map<String, String> parameters;
        if (params.values().iterator().next() instanceof String[]) {
            parameters = Maps.transformValues(params, StringArrayToFirstValueFunction.INSTANCE);
        } else {
            parameters = params;
        }
        return parameters;
    }

    public JsonBuilder withParameters(Map params) {
        Map<String, String> parameters = requestParametersMapToParametersMap(params);
        String startStr = parameters.get(ExtjsConst.PARAM_START);
        start = Strings.isNullOrEmpty(startStr) ? 0 : Integer.valueOf(startStr);
        String limitStr = parameters.get(ExtjsConst.PARAM_LIMIT);
        limit = Strings.isNullOrEmpty(limitStr) ? Integer.MAX_VALUE : Integer.valueOf(limitStr);
        sorter = buildComparator(parameters.get(ExtjsConst.PARAM_SORT));
        String filterParam = Strings.emptyToNull(parameters.get(ExtjsConst.PARAM_FILTER));
        if (filterParam != null) {
            logger.debug("filtering results with pattern = '{}'", filterParam);
            filter = buildBeanFilter(filterParam);
        }
        return this;
    }

    private Iterable filter(Iterable data) {
        if (filter == null) {
            return data;
        }
        return Iterables.filter(data, filter);
    }

    private Iterable sort(Iterable data) {
        if (sorter == null) {
            return data;
        } else {
            List list = Lists.newArrayList(data);
            Collections.sort(list, sorter);
            return list;
        }
    }

    private Iterable trim(Iterable data) {
        if (start == null || limit == null) {
            return data;
        }
        return Iterables.limit(Iterables.skip(data, start), limit);
    }

    private Iterable transform(Iterable data) {
        if (transformer == null) {
            return data;
        }
        return Iterables.transform(data, transformer);
    }

    private JsonStoreResponse prepareStoreResponse() {
        Iterable storeData;
        if (data == null) {
            storeData = Collections.emptyList();
        } else if (data instanceof Iterable) {
            storeData = (Iterable) data;
        } else {
            storeData = Collections.singletonList(data);
        }

        Integer tot;

        if (filterEarly) {
            storeData = filter(storeData);
        }
        if (sortEarly) {
            storeData = sort(storeData);
        }
        if ((sortEarly || sorter == null) && (filterEarly || filter == null)) {
            tot = Iterables.size(storeData);
            storeData = transform(trim(storeData));
        } else {
            storeData = transform(storeData);
            if (!filterEarly) {
                storeData = filter(storeData);
            }
            if (!sortEarly) {
                storeData = sort(storeData);
            }
            tot = Iterables.size(storeData);
            storeData = trim(storeData);
        }

        tot = MoreObjects.firstNonNull(total, tot);
        JsonStoreResponse jsonStoreResponse = new JsonStoreResponse(storeData instanceof Collection ? (Collection) storeData : Lists.newArrayList(storeData), tot);
        jsonStoreResponse.setMessage(message);
        jsonStoreResponse.setSuccess(success);
        return jsonStoreResponse;
    }

    public JsonStoreResponse buildStoreResponse() {
        return writeOutput(prepareStoreResponse());
    }
    

    /**
     * serialize and write parameter as gson object
     *
     * @param jsonObject
     */
    public void sendResponse(Object jsonObject) {
        Preconditions.checkNotNull(writer, "writer not set");
        writeOutput(jsonObject);
    }
    
    // check se una string è una data per l'ordinamento...  
    private static boolean isValidDate(Object inDate) {
    if(inDate instanceof String){
    String inDateStr = (String) inDate;
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      dateFormat.setLenient(false);
      try {
        dateFormat.parse(inDateStr);
      } catch (ParseException pe) {
        return false;
      }
      return true;
    }
    else {
    	return false;
    }
    }


    private <T> T writeOutput(T t) {
        if (writer != null) {
            try {
                gson.toJson(t, writer);
                writer.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return t;
    }

    @Deprecated
    public static JsonResponse buildErrorResponse(String message) {
        return newInstance().withError(message).buildResponse();
    }

    @Deprecated
    public static JsonResponse buildErrorResponse(Throwable exception) {
        return newInstance().withError(exception).buildResponse();
    }


    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data) {
        return buildStoreResponse(data, null, null, null);
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Integer total, @Nullable Function<E, ? extends Object> transformer, @Nullable Comparator<E> sorter) {
        Iterable newData = sortIfNeeded(data, sorter);
        if (transformer != null) {
            newData = Iterables.transform(data, transformer);
        }
        List list = Lists.newArrayList(newData);
        return new JsonStoreResponse(list, total != null ? total : list.size());
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Integer start, @Nullable Integer limit, @Nullable Function<E, ? extends Object> transformer, JsonSortInfo... sortInfoList) {
        return buildStoreResponse(data, start, limit, transformer, sortInfoList == null || sortInfoList.length == 0 ? null : buildComparator(sortInfoList));
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Integer start, @Nullable Integer limit, @Nullable Function<E, ? extends Object> transformer, String sortInfoListJson) {
        return buildStoreResponse(data, start, limit, transformer, buildComparator(sortInfoListJson));
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Function<E, ? extends Object> transformer, Map params) {
        Map<String, String> parameters;
        if (params.values().iterator().next() instanceof String[]) {
            parameters = Maps.transformValues(params, StringArrayToFirstValueFunction.INSTANCE);
        } else {
            parameters = params;
        }
        return buildStoreResponse(data, Integer.valueOf(parameters.get(ExtjsConst.PARAM_START)), Integer.valueOf(parameters.get(ExtjsConst.PARAM_LIMIT)), transformer, parameters.containsKey(ExtjsConst.PARAM_SORT) ? buildComparator(parameters.get(ExtjsConst.PARAM_SORT)) : null);
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Integer start, @Nullable Integer limit, @Nullable Function<E, ? extends Object> transformer, @Nullable Comparator<E> sorter) {
        return buildStoreResponse(data, start, limit, transformer, sorter, true);
    }

    @Deprecated
    public static <E> JsonStoreResponse buildStoreResponse(Iterable<E> data, @Nullable Integer start, @Nullable Integer limit, @Nullable Function<E, ? extends Object> transformer, @Nullable Comparator<E> sorter, boolean sortEarly) {

        Integer total = Iterables.size(data);
        if (sortEarly) {
            data = sortIfNeeded(data, sorter);
        }
        if (!sortEarly && transformer != null) {
            data = (Iterable) Iterables.transform(data, transformer);
        }

        if (start != null && limit != null) {
            data = Iterables.limit(Iterables.skip(data, start), limit);
        }
        if (!sortEarly) {
            data = sortIfNeeded(data, sorter);
        }
        return buildStoreResponse(data, total, sortEarly ? transformer : null, null);
    }

    @Deprecated
    private static <E> Iterable<E> sortIfNeeded(Iterable<E> data, @Nullable Comparator<E> sorter) {
        if (sorter != null) {
            List<E> list = Lists.newArrayList(data);
            Collections.sort(list, sorter);
            data = list;
        }
        return data;
    }

    public JsonResponse buildResponse() {
        Validate.isTrue(data == null || mapData == null, "cannot set both data and mapData");
        JsonResponse resp;
        if (data != null || mapData != null) {
            Object obj = MoreObjects.firstNonNull(data, mapData);
            if (transformer != null) {
                obj = transformer.apply(obj);
            }
            resp = new JsonDataResponse(obj);
        } else {
            resp = new JsonResponse();
        }
        resp.setMessage(message);
        resp.setSuccess(success);
        return writeOutput(resp);
    }

    public static JsonResponse buildResponse(String message) {
        return new JsonResponse(message);
    }

    public static JsonResponse buildResponse(String message, Object data) {
        return new JsonDataResponse(message, data);
    }

    public static JsonSortInfo[] parseSortInfo(@Nullable String sortInfoListJson) {
        return Strings.isNullOrEmpty(sortInfoListJson) ? new JsonSortInfo[0] : (JsonSortInfo[]) gson.fromJson(sortInfoListJson, JsonSortInfo.SORT_INFO_LIST_TYPE);
    }

    public static Comparator buildComparator(@Nullable String sortInfoListJson) {
        return Strings.isNullOrEmpty(sortInfoListJson) ? null : buildComparator(parseSortInfo(sortInfoListJson));
    }

    @Deprecated
    public static Comparator buildMapComparator(String field) {
        return buildComparator(new JsonSortInfo(field));
    }

    public static Comparator buildComparator(final JsonSortInfo... sortInfoList) {
        Preconditions.checkArgument(sortInfoList != null && sortInfoList.length > 0, "invalid JsonSortInfo = " + sortInfoList);
        return Ordering.compound(Iterables.transform(Arrays.asList(sortInfoList), new Function<JsonSortInfo, Comparator<Object>>() {
            public Comparator apply(final JsonSortInfo sortInfo) {
                return new Comparator() {
                    final String propertyName = sortInfo.getProperty();
                    final int ord = Objects.equal(sortInfo.getDirection(), JsonSortInfo.ASC) ? 1 : -1;

                    public int compare(Object o1, Object o2) {
                        Object val1 = getProperty(o1, propertyName), val2 = getProperty(o2, propertyName);
                        if (val1 == val2) {
                            return 0;
                        } else if (val1 == null) {
                            return ord * 1;
                        } else if (val2 == null) {
                            return ord * -1;
                        }
                        else if (isValidDate(val1) && isValidDate(val2)){
                        	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        	Date first;
							try {
								first = df.parse((String)val1);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								return 0;
							}
                        	Date second;
							try {
								second = df.parse((String)val2);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return 0;
							}
                        	if(first.before(second)){
                        		return ord * -1;
                        	}
                        	else if(first.after(second)){
                        		return ord * 1;
                        	}
                        	else {
                        		return 0;
                        	}
                        	
                        		
                        }
                        else if (val1 instanceof Comparable && val2 instanceof Comparable) {
                            return ord * Ordering.natural().compare((Comparable) val1, (Comparable) val2);
                        } else {
                            return ord * Ordering.usingToString().compare(val1, val2);
                        }
                    }

                    private Object getProperty(Object object, String property) {
                        try {
                            return PropertyUtils.getProperty(object, property);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };
            }
        }));
    }

    public List buildListResponse() {
        Collection dataObj = prepareStoreResponse().getData();
        List list = dataObj instanceof List ? ((List) dataObj) : Lists.newArrayList(dataObj);
        return writeOutput(list);
    }

    private static enum StringArrayToFirstValueFunction implements Function<String[], String> {

        INSTANCE;

        public String apply(String[] input) {
            if (input.length == 0) {
                logger.warn("got less than one value in array, while mapping String[] -> String ; returning null");
                return null;
            } else {
                if (input.length > 1) {
                    logger.warn("got more than one value in array, while mapping String[] -> String ({}) ;  returning first", input);
                }
                return input[0];
            }
        }
    }

    public static Predicate<Object> buildBeanFilter(@Nullable final String regexpArg, final @Nullable String... restrictFields) {
        Predicate<Map> mapFilter = buildMapFilter(regexpArg, restrictFields);
        return mapFilter == null ? null : Predicates.compose(mapFilter, new Function<Object, Map>() {
            public Map apply(Object input) {
                if (input instanceof Map) {
                    return (Map) input;
                }
                try {
                    return PropertyUtils.describe(input);
                } catch (Exception ex) {
                    logger.warn("exception while converting bean as properties map", ex);
                    return Collections.emptyMap();
                }
            }
        });
    }

    /**
     * costruisce un filtro su oggetti (rappresentati da mappe) basandosi su una
     * regexp; la regexp viene splittata sugli spazi, inoltre i segmenti che
     * iniziano con ! vengono interpretati come negazioni. Valida gli oggetti
     * che hanno, per ogni segmento di regexp, almeno un campo valido, e non
     * hanno nessun campo valido per ogni segmento di regexp negata.
     *
     * @param regexpArg se null, restituisce null (per consistenza con gli altri
     * metodi)
     * @param restrictFields se valorizzato, restringe i campi della mappa su
     * cui effettuare i test
     * @return
     */
    public static Predicate<Map> buildMapFilter(@Nullable final String regexpArg, final @Nullable String... restrictFields) {
        return StringUtils.isBlank(regexpArg) ? null : Predicates.compose(Predicates.and(Iterables.transform(Lists.newArrayList(regexpArg.split(" ")), new Function<String, Predicate<Iterable<CharSequence>>>() {
            public Predicate<Iterable<CharSequence>> apply(final String regexp) {
                return new Predicate<Iterable<CharSequence>>() {
                    final boolean negate = regexp.startsWith("!");
                    final Pattern pattern = Pattern.compile(negate ? regexp.substring(1) : regexp, Pattern.CASE_INSENSITIVE);
                    final Predicate<CharSequence> predicate = Predicates.contains(pattern);

                    public boolean apply(Iterable<CharSequence> input) {
                        return negate != Iterables.any(input, predicate);

                    }
                };
            }
        })), (restrictFields == null || restrictFields.length == 0) ? new Function<Map, Iterable<CharSequence>>() {
            public Iterable<CharSequence> apply(Map input) {
                return Lists.newArrayList(Iterables.transform(input.values(), ObjectToStringFunction.INSTANCE));
            }
        } : new Function<Map, Iterable<CharSequence>>() {
            Set<String> keys = Sets.newHashSet(restrictFields);

            public Iterable<CharSequence> apply(Map input) {
                return Lists.newArrayList(Iterables.transform(Maps.filterKeys(input, Predicates.in(keys)).values(), ObjectToStringFunction.INSTANCE));
            }
        });
    }

    private static enum ObjectToStringFunction implements Function<Object, String> {

        INSTANCE;

        public String apply(Object input) {
            return input == null ? "" : input.toString();
        }
    }

    private static enum GenericObjectToMapFunction implements Function<Object, Map> {

        INSTANCE;

        public Map apply(Object input) {
            try {
                return Maps.transformValues(PropertyUtils.describe(input), ObjectToStringFunction.INSTANCE);
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public static Function<Object, Map> getGenericObjectToMapFunction() {
        return GenericObjectToMapFunction.INSTANCE;
    }

    @Deprecated
    public static Type getStringMapType() {
        return MAP_OF_STRINGS;
    }

    @Deprecated
    public static Type getStringListType() {
        return LIST_OF_STRINGS;
    }

    @Deprecated
    public static Type getListOfStringMapType() {
        return LIST_OF_MAP_OF_STRINGS;
    }
    public static final Type MAP_OF_STRINGS = new TypeToken<Map<String, String>>() {
    }.getType(), LIST_OF_STRINGS = new TypeToken<List<String>>() {
    }.getType(), LIST_OF_MAP_OF_STRINGS = new TypeToken<List<Map<String, String>>>() {
    }.getType();
}
