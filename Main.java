import crashtable.CrashTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) throws IOException {

		// The format is [["Key", "Value"]]
		List<String[]> list = new ArrayList<>();

		// Table path argument provided
		if (args.length > 0) {
			list = Files.lines(Path.of(args[0].trim()))
					.map(l -> l.split("\\s+=>\\s+"))
					.collect(Collectors.toList());
		}
		//System.out.print(list.get(0)[0]);
		var table = new CrashTable();
		
		for(int i=0 ; i < 55 ; i++) {
			table.put(list.get(i)[0], list.get(i)[1]);
		}
		table.print();
		//System.out.print(Arrays.toString(table.getKeys()));
	}
}