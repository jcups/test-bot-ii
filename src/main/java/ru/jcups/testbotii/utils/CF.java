package ru.jcups.testbotii.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CF {

	public static final String path = "src/main/resources/static/codes.json";

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final Map<String, Map<String, List<String>>> codes = new TreeMap<>();

	static {
		initCodes();
	}

	private static void initCodes() {
		File file = new File(path);
		loadFromJson(file);
	}

	private static void loadFromJson(File file) {
		ObjectReader reader = mapper.readerForMapOf(codes.getClass());
		try {
			Object value = reader.readValue(file);
			codes.putAll((Map<? extends String, ? extends Map<String, List<String>>>) value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void loadFromTxtFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while (reader.ready()) {
			line = reader.readLine();
			String code = line.substring(0, 7);
			String name = line.substring(8);
			String[] split = code.split("-");
			String first, second;
			if (split.length == 2) {
				first = split[0];
				second = split[1];
			} else {
				throw new RuntimeException();
			}
			Map<String, List<String>> map = new TreeMap<>();
			List<String> list = new LinkedList<>();

			if (codes.containsKey(first)) {
				map = codes.get(first);
				if (map.containsKey(second)) {
					list = map.get(second);
				}
			}

			list.add(name);
			map.put(second, list);
			codes.put(first, map);
		}
	}

	private static void save(Map<String, Map<String, List<String>>> codes) {
		File file = new File(path);
		try {
			mapper.writeValue(file, codes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String[] get(String first, String second) {
		Map<String, List<String>> map = codes.get(first);
		if (map != null && map.containsKey(second)) {
			return map.get(second).toArray(new String[0]);
		}
		return null;
	}
}
