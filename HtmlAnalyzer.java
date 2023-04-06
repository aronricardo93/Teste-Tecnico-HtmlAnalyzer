import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class HtmlAnalyzer {
	public static void main(String[] args) throws IOException {
		try{	
			String url = args[0].toString();
	        				String html = getHtml(url);
			String conteudo = getConteudo(html);
			System.out.println(conteudo);			
		}catch(Exception e) {
			System.err.println("URL connection error");
		}
    }
	
    private static String getHtml(String url) throws IOException {
        URLConnection conexao = new URL(url).openConnection();
        InputStream inputStream = conexao.getInputStream();
        byte[] bytes = inputStream.readAllBytes();
        return new String(bytes);
    }

    private static String getConteudo(String html) {
        String tag = getTagMaisInterna(html);
        return getTag(html, tag);
    }

    private static String getTagMaisInterna(String html) {
        int profundidadeMaxima = 0;
        String tagMaisInterna = "";
        int profundidade = 0;
        for (int i = 0; i < html.length(); i++) {
            if (html.charAt(i) == '<') {
                int j = i + 1;
                boolean isTagFechada = false;
                if (html.charAt(j) == '/') {
                    j++;
                    isTagFechada = true;
                }
                StringBuilder tagNomeBuilder = new StringBuilder();
                while (j < html.length() && html.charAt(j) != '>') {
                    tagNomeBuilder.append(html.charAt(j));
                    j++;
                }
                String tagNome = tagNomeBuilder.toString();
                if (!isTagFechada) {
                    profundidade++;
                    if (profundidade > profundidadeMaxima) {
                        profundidadeMaxima = profundidade;
                        tagMaisInterna = tagNome;
                    }
                } else {
                    profundidade--;
                }
                i = j;
            }
        }
        return tagMaisInterna;
    }

    private static String getTag(String html, String tag) {
        int inicio = html.indexOf("<" + tag + ">") + tag.length() + 2;
        int fim = html.indexOf("</" + tag + ">");
        return html.substring(inicio, fim).trim();
    }
}

