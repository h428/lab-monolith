package com.lab;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lab.common.component.TokenUtil;
import com.lab.common.constant.MyHttpHeader;
import com.lab.common.util.ClassUtil;
import com.lab.common.util.JacksonUtil;
import com.lab.common.bean.PageBean;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Controller 层的测试类，提供 Mock 测试环境，所有 Controller 的测试类都继承该类而自动具有环境配置
 *
 * 为了方便测试，在 MockMvc 封装请求，以便快速测试
 *
 * @author hao
 */
public class BaseWebTest extends BaseTest {

    public static final String BASE_USER_HEADER = "X-BASE-USER-ID";
    protected static String lyhToken;

    @Autowired
    WebApplicationContext wac;

    // 主要通过 mockMvc 完成对 http 请求的测试
    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        lyhToken = TokenUtil.baseUserLogin(1L);
    }

    /**
     * 构造 get 请求，注意执行 execute 获得封装的 Response
     *
     * @param url 请求地址
     * @param vars 路径参数
     * @return 封装的 Request
     */
    protected Request get(String url, Object... vars) {
        return new Request(HttpMethod.GET, url, vars);
    }

    /**
     * 构造 post 请求，注意执行 execute 获得封装的 Response
     *
     * @param url 请求地址
     * @param vars 路径参数
     * @return 封装的 Request
     */
    protected Request post(String url, Object... vars) {
        return new Request(HttpMethod.POST, url, vars);
    }

    /**
     * 构造 put 请求，注意执行 execute 获得封装的 Response
     *
     * @param url 请求地址
     * @param vars 路径参数
     * @return 封装的 Request
     */
    protected Request put(String url, Object... vars) {
        return new Request(HttpMethod.PUT, url, vars);
    }

    /**
     * 构造 delete 请求，注意执行 execute 获得封装的 Response
     *
     * @param url 请求地址
     * @param vars 路径参数
     * @return 封装的 Request
     */
    protected Request delete(String url, Object... vars) {
        return new Request(HttpMethod.DELETE, url, vars);
    }

    public class Request {

        private MockHttpServletRequestBuilder requestBuilder;

        /**
         * 根据请求类型和请求地址构造请求
         *
         * @param method 请求类型
         * @param url 请求地址
         * @param vars 路径参数
         */
        public Request(HttpMethod method, String url, Object... vars) {
            requestBuilder = MockMvcRequestBuilders.request(method, url, vars);
        }

        /**
         * 设置请求头
         *
         * @param headerName 请求头名称
         * @param headerValue 请求头值
         * @return 返回 Request 以链式调用
         */
        public Request header(String headerName, String headerValue) {
            // 设置请求头
            requestBuilder.header(headerName, headerValue);
            return this;
        }

        /**
         * 设置请求头 Authorization
         *
         * @param token token 值
         * @return 返回 Request 以链式调用
         */
        public Request token(String token) {
            // 设置请求头
            requestBuilder.header(MyHttpHeader.AUTHORIZATION, token);
            return this;
        }

        /**
         * 设置请求头 Authorization
         *
         * @param baseUserId baseUserId 值
         * @return 返回 Request 以链式调用
         */
        public Request baseUser(String baseUserId) {
            // 设置请求头
            requestBuilder.header(BASE_USER_HEADER, baseUserId);
            return this;
        }

        /**
         * 设置请求体
         *
         * @param body 请求体
         * @return 返回 Request 以链式调用
         */
        public Request body(Object body) {
            // 请求体转化为 json
            String json = JacksonUtil.toJson(body);

            // 请求体不为空则本次请求添加请求体，类型为 application/json;charset=UTF-8
            if (json != null) {
                requestBuilder.contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(json);
            }
            return this;
        }

        /**
         * 设置请求的查询参数
         *
         * @param paramName 参数名
         * @param paramValue 参数值
         * @return 返回 Request 以链式调用
         */
        public Request param(String paramName, String paramValue) {
            requestBuilder.param(paramName, paramValue);
            return this;
        }

        /**
         * 根据 object 设置请求参数
         *
         * @param object 设置实体
         * @return 返回 Request 以链式调用
         */
        public Request param(Object object) {
            Map<String, Object> properties = ClassUtil.parseToMap(object);
            properties.forEach((k, v) -> {
                if (v != null) {
                    param(k, String.valueOf(v));
                }
            });
            return this;
        }

        /**
         * 执行请求，拿到本次请求的响应
         *
         * @return 返回本次请求的响应 Response，其支持链式调用以链式判定
         */
        public Response execute() {
            try {
                return new Response(mockMvc.perform(requestBuilder));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 对 ResultActions 的封装，方便自己操作和测试以及链式校验
     */
    public class Response {

        private ResultActions resultActions;

        // 必须提供 ResultActions 实例，该类在其基础上封装，用于快速测试
        public Response(ResultActions resultActions) {
            this.resultActions = resultActions;
        }

        /**
         * 校验响应状态码
         *
         * @param status 响应状态码
         * @return 返回 Result 以便能够链式校验
         */
        public Response status(int status) {
            try {
                resultActions.andExpect(MockMvcResultMatchers.status().is(status));
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 校验响应体中 json 的对应属性存在
         *
         * @param propertyName 属性名称
         * @return 返回 Result 以便能够链式校验
         */
        public Response propertyExists(String propertyName) {
            try {
                resultActions.andExpect(jsonPath("$." + propertyName).exists());
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 校验响应体中 json 的对应属性值
         *
         * @param propertyName 属性名
         * @param expectedValue 属性值
         * @return 返回 Result 以便能够链式校验
         */
        public Response property(String propertyName, Object expectedValue) {
            try {
                resultActions.andExpect(jsonPath("$." + propertyName).value(expectedValue));
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 校验响应为 ResponseWrapper 格式
         *
         * @return 返回 Result 以便能够链式校验
         */
        public Response validateResponseWrapper() {
            return this.propertyExists("status").propertyExists("message");
        }

        /**
         * 打印该响应
         *
         * @return 返回 Result 以便能够链式校验
         */
        public Response print() {
            try {
                resultActions.andDo(MockMvcResultHandlers.print());
                return this;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为对应类型
         *
         * @param clazz 目标类别
         * @param <T> 泛型 T 即结果类型
         * @return 返回反序列化的结果
         */
        public <T> T getBean(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                return JSONUtil.toBean(json, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为对应类型
         *
         * @param clazz 目标类别
         * @param <T> 泛型 T 即结果类型
         * @return 返回反序列化的结果
         */
        public <T> T getBeanData(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                JSON parse = JSONUtil.parse(json);
                // 取出 data 属性
                JSONObject data = JSONUtil.getByPath(parse, "data", null);
                return JSONUtil.toBean(data, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为对应类型
         *
         * @param clazz 目标类别
         * @param <T> 泛型 T 即结果类型
         * @return 返回反序列化的结果
         */
        public <T> List<T> getListData(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                JSON parse = JSONUtil.parse(json);
                // 取出 data 属性
                JSONArray data = JSONUtil.getByPath(parse, "data", null);
                return JSONUtil.toList(data, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public <T> PageBean<T> getPageBeanData(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                JSON parse = JSONUtil.parse(json);
                // 取出 data 属性
                JSONObject data = JSONUtil.getByPath(parse, "data", null);
                TypeReference<PageBean<T>> typeReference = new TypeReference<PageBean<T>>(){};
                return JSONUtil.toBean(data, typeReference, true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为列表类型
         *
         * @param clazz 目标类别
         * @param <T> 泛型 T 元类型，一般是实体
         * @return 返回反序列化的列表
         */
        public <T> List<T> getList(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                return JacksonUtil.fromJsonToList(json, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为集合类型
         *
         * @param clazz 目标类别
         * @param <T> 泛型 T 元类型，一般是实体
         * @return 返回反序列化的列表
         */
        public <T> Set<T> getSet(Class<T> clazz) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                return JacksonUtil.fromJsonToSet(json, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 泛型方法，取出响应体 json 并反序列化为集合类型
         *
         * @param keyClass 键的类型
         * @param valueClass 值的类型
         * @param <K> 键泛型
         * @param <V> 值泛型
         * @return 返回 map
         */
        public <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) {
            try {
                String json = resultActions.andReturn().getResponse().getContentAsString();
                return JacksonUtil.fromJsonToMap(json, keyClass, valueClass);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
