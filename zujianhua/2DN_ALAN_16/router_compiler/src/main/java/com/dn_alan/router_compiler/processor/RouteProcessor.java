package com.dn_alan.router_compiler.processor;

import com.dn_alan.router_annotation.Route;
import com.dn_alan.router_annotation.model.RouteMeta;
import com.dn_alan.router_compiler.utils.Consts;
import com.dn_alan.router_compiler.utils.Log;
import com.dn_alan.router_compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.org.apache.xml.internal.serializer.Method;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 在这个类上添加了@AutoService注解，它的作用是用来生成
 * META-INF/services/javax.annotation.processing.Processor文件的，
 * 也就是我们在使用注解处理器的时候需要手动添加
 * META-INF/services/javax.annotation.processing.Processor，
 * 而有了@AutoService后它会自动帮我们生成。
 * AutoService是Google开发的一个库，使用时需要在
 * factory-compiler中添加依赖
 */
@AutoService(Processor.class)
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
/**
 * 注册给哪些注解的  替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes(Consts.ANN_TYPE_ROUTE)

public class RouteProcessor extends AbstractProcessor {
    private Log log;
    /**
     * 文件生成器
     */
    private Filer filerUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtile;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementsUtils;

    /**
     * 分组 key:组名 value:对应的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    /**
     * 初始化
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        log = Log.newLog(processingEnvironment.getMessager());
        filerUtils = processingEnvironment.getFiler();
        typeUtile = processingEnvironment.getTypeUtils();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 相当于main函数，正式处理注解
     *
     * @param set              使用了支持处理注解  的节点集合
     * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //使用了需要处理的注解
        if (!Utils.isEmpty(set)) {
            //获取所有被Route 注解的元素集合
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(Route.class);
            //处理Route注解
            if (!Utils.isEmpty(elementsAnnotatedWith)) {
                log.i("Route Class: ===" + elementsAnnotatedWith.size());
                parseRoutes(elementsAnnotatedWith);
            }
            return true;
        }
        return false;
    }

    private void parseRoutes(Set<? extends Element> anotatedWith) {
        //elementsUtils 通过节点工具，传入全类名，生成节点 （activity）
        TypeElement typeElement = elementsUtils.getTypeElement(Consts.ACTIVITY);
        //节点的描述信息
        TypeMirror type_activity = typeElement.asType();

        for (Element element : anotatedWith) {
            RouteMeta routeMeta = null;
            //使用Route注解的类信息（acvitiy）
            TypeMirror tm = element.asType();

            Route route = element.getAnnotation(Route.class);

            //判断注解使用什么类上面
            if (typeUtile.isSubtype(tm, type_activity)) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else {
                throw new RuntimeException("[Just Support Activity/IService Route]:" + element);
            }

            /**
             * 分组
             */
            categories(routeMeta);
        }

        //生成类需要实现的接口
        TypeElement iRouteCroup = elementsUtils.getTypeElement(Consts.IROUTE_GROUP);

        /**
         *生成Group类
         */
        generatedGroup(iRouteCroup);

    }

    private void generatedGroup(TypeElement iRouteCroup) {
        //创建参数类型  Map<MapString, RouteMeta>
        ParameterizedTypeName atlas = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteMeta.class));

        //创建参数名字  Map<MapString, RouteMeta> atlas
        ParameterSpec spec = ParameterSpec.builder(atlas, "atlas").build();

        //遍历分组， 每一个分组创建一个   ...$$$$Group$$ 类
        for (Map.Entry<String, List<RouteMeta>> stringListEntry : groupMap.entrySet()) {

            //方法   public void loadInto(Map<MapString, RouteMeta> atlas) {}
            MethodSpec.Builder builder = MethodSpec.methodBuilder(Consts.METHOD_LOAD_INTO)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(spec);

            String groupName = stringListEntry.getKey();
            List<RouteMeta> groupData = stringListEntry.getValue();

            for (RouteMeta routeMeta : groupData) {
                /**
                 * atlas.put("/main/test", RouteMeta.build(
                 * RouteMeta.Type.ACTIVITY,SecondActivity.class, "/main/test", "main"));
                 */
                builder.addStatement("atlas.put($S,$T.build($T.$L,$T.class,$S, $S)",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get((TypeElement) routeMeta.getElement()),
                        routeMeta.getPath().toLowerCase(),
                        routeMeta.getGroup().toLowerCase());
            }

            //DNRouter$$Group$$main
            String groupClassName = Consts.NAME_OF_GROUP + groupName;
            try {
                JavaFile.builder(Consts.PACKAGE_OF_GENERATE_FILE,
                        TypeSpec.classBuilder(groupClassName)
                .addSuperinterface(ClassName.get(iRouteCroup))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(builder.build()).build()).build().writeTo(filerUtils);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            List<RouteMeta> routeMetas = groupMap.get(routeMeta.getGroup());
            //如果为记录分组则创建
            if (Utils.isEmpty(routeMetas)) {
                List<RouteMeta> routeMetaList = new ArrayList<>();
                routeMetaList.add(routeMeta);
                groupMap.put(routeMeta.getGroup(), routeMetaList);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            log.i("Group Info Error : " + routeMeta.getPath());
        }

    }

    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();
        if (Utils.isEmpty(path) || !path.startsWith("/")) {
            return false;
        }
        if (Utils.isEmpty(group)) {
            String substring = path.substring(1, path.indexOf("/", 1));
            if (Utils.isEmpty(substring)) {
                return false;
            }
            routeMeta.setGroup(substring);
            return true;
        }
        return true;
    }


}
