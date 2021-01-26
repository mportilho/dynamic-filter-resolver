package com.github.dfr.provider.specification.decoder.type;
//package com.github.djs.provider.specification.decoder.type;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//import javax.persistence.criteria.From;
//import javax.persistence.criteria.Join;
//import javax.persistence.criteria.Path;
//import javax.persistence.criteria.Root;
//
//import org.springframework.data.mapping.PropertyPath;
//
//import com.github.djs.filter.ParameterFilterMetadata;
//
//class SpecSharedContextUtils {
//
//	private static final String JOIN_CACHE = "JOIN_CACHE";
//
//	private SpecSharedContextUtils() {
//	}
//
//	
//
////	/**
////	 * 
////	 * @param alias
////	 * @param sharedContext
////	 * @param joinSupplier
////	 */
////	public static final void putJoin(String alias, Map<String, Object> sharedContext, Function<Root<?>, Join<?, ?>> joinSupplier) {
////		getJoinCache(sharedContext).putIfAbsent(alias, joinSupplier);
////	}+
//	
//
//	/**
//	 * 
//	 * @param sharedContext
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private static final Map<String, Function<Root<?>, Join<?, ?>>> getJoinCache(Map<String, Object> sharedContext) {
//		Map<String, Function<Root<?>, Join<?, ?>>> joinCache = (Map<String, Function<Root<?>, Join<?, ?>>>) sharedContext.computeIfAbsent(JOIN_CACHE,
//				k -> new HashMap<>());
//		return joinCache;
//	}
//
//}
