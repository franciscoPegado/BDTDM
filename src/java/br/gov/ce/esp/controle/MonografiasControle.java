/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.pojos.Linhaspesquisa;
import br.gov.ce.esp.pojos.Monografias;
import br.gov.ce.esp.pojos.Programas;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.event.ActionEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.hibernate.Query;

/**
 *
 * @author francisco
 */
@ManagedBean(name = "monografiaControle")
@SessionScoped
public class MonografiasControle {

    private Monografias monografias = null;
    private Linhaspesquisa linhasPesquisa = null;
    private Programas programas = null;
    private Session session = null;
    private Transaction tx = null;
    private DataModel model = null;
    private HtmlSelectOneMenu htmlSelectOneMenu = null;
    private String nomeArquivoSelecionado = null;
    private String dirAbsoluto = "D:/projetos/gemono/web/monografias/";
    private String desc = "";
    private boolean exibirForm = false;
    private HtmlSelectOneMenu selectOneMenuLinhasPesquisas = null;
    private HtmlSelectOneMenu selectOneMenuPrograma = null;

    public MonografiasControle() {
        monografias = new Monografias();
        linhasPesquisa = new Linhaspesquisa();
        programas = new Programas();
    }

    public Programas getProgramas() {
        return programas;
    }

    public void setProgramas(Programas programas) {
        this.programas = programas;
    }

    public Linhaspesquisa getLinhasPesquisa() {
        return linhasPesquisa;
    }

    public void setLinhasPesquisa(Linhaspesquisa linhasPesquisa) {
        this.linhasPesquisa = linhasPesquisa;
    }

    public Monografias getMonografias() {
        return monografias;
    }

    public void setMonografias(Monografias monografias) {
        this.monografias = monografias;
    }

    public DataModel getModel() {
        return model = new ListDataModel(PesquisaMonografias());
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public HtmlSelectOneMenu getHtmlSelectOneMenu() {
        return htmlSelectOneMenu;
    }

    public void setHtmlSelectOneMenu(HtmlSelectOneMenu htmlSelectOneMenu) {
        this.htmlSelectOneMenu = htmlSelectOneMenu;
    }

    public String getNomeArquivoSelecionado() {
        return nomeArquivoSelecionado;
    }

    public void setNomeArquivoSelecionado(String nomeArquivoSelecionado) {
        this.nomeArquivoSelecionado = nomeArquivoSelecionado;
    }

    public boolean isExibirForm() {
        return exibirForm;
    }

    public void setExibirForm(boolean exibirForm) {
        this.exibirForm = exibirForm;
    }

    public HtmlSelectOneMenu getSelectOneMenuLinhasPesquisas() {
        return selectOneMenuLinhasPesquisas;
    }

    public void setSelectOneMenuLinhasPesquisas(HtmlSelectOneMenu selectOneMenuLinhasPesquisas) {
        this.selectOneMenuLinhasPesquisas = selectOneMenuLinhasPesquisas;
    }

    public HtmlSelectOneMenu getSelectOneMenuPrograma() {
        return selectOneMenuPrograma;
    }

    public void setSelectOneMenuPrograma(HtmlSelectOneMenu selectOneMenuPrograma) {
        this.selectOneMenuPrograma = selectOneMenuPrograma;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void incluir(ActionEvent actionEvent) {
        if (monografias.getCodigo() == 0) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                monografias = new Monografias(linhasPesquisa, programas, monografias.getTlptbr(), monografias.getTlpeng(), monografias.getAutor(),
                        monografias.getOrientador(), monografias.getCoorientador(), monografias.getBncex(), monografias.getDatadefesa(),
                        monografias.getResumo(), monografias.getAbstractresumo(), monografias.getCaminho());
                session.save(monografias);
                tx.commit();
                FacesUtils.mostrarMensagem("Monografia incluída com sucesso!");

            } catch (HibernateException hbe) {
                FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
            } finally {
                if (session != null) {
                    session.close();
                    monografias = new Monografias();
                }
            }
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            excluirArquivo();
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            monografias = getProdutoEditarExcluir();
            session.delete(monografias);
            tx.commit();
            FacesUtils.mostrarMensagem("Monografia excluída com sucesso!");
            session.close();
            monografias = new Monografias();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir a monografia" + ex);
        }
    }

    public List<SelectItem> getListarPesquisa() {
        List<Linhaspesquisa> arrayLinhasPesquisa = null;
        List<SelectItem> listaLinhasPesquisa = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Query q = session.createQuery("from Linhaspesquisa order by nome");
            arrayLinhasPesquisa = q.list();
            listaLinhasPesquisa = new ArrayList<SelectItem>();
            listaLinhasPesquisa.add(new SelectItem("0", "--Selecione--"));
            for (Linhaspesquisa item : arrayLinhasPesquisa) {
                listaLinhasPesquisa.add(new SelectItem(item.getCodigo(), item.getNome()));
            }
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao listar" + "\n" + ex);
        }
        return listaLinhasPesquisa;
    }

    public List<SelectItem> getListarPrograma() {
        List<Programas> arrayPrograma = null;
        List<SelectItem> listaPrograma = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Query q = session.createQuery("from Programas as p order by p.nomeprograma");
            arrayPrograma = q.list();
            listaPrograma = new ArrayList<SelectItem>();
            listaPrograma.add(new SelectItem("0", "--Selecione--"));
            for (Programas itemPrograma : arrayPrograma) {
                listaPrograma.add(new SelectItem(itemPrograma.getCodigo(), itemPrograma.getNomeprograma()));
            }
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao listar" + "\n" + ex);
        }
        return listaPrograma;
    }

    public DataModel<Monografias> getListarMonografias() {
        List<Monografias> arrayMonografias = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Monografias order by autor");
            arrayMonografias = q.list();
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro" + "\n" + ex);
        }
        return model = new ListDataModel(arrayMonografias);
    }

    public void fileUploadAction(FileUploadEvent event) {
        if (event.getFile().getFileName().toLowerCase().endsWith(".pdf")) {
            try {
                setNomeArquivoSelecionado(event.getFile().getFileName());
                UploadedFile arq = event.getFile();
                InputStream in = new BufferedInputStream(arq.getInputstream());
                File file = new File(dirAbsoluto + monografias.getAutor() + "/" + monografias.getTlptbr());
                if (file.exists()) {
                    deletaDir(file);
                }
                file.mkdirs();
                File file1 = new File(file, event.getFile().getFileName());
                monografias.setCaminho(file1.getAbsolutePath());
                FileOutputStream fout = new FileOutputStream(file1);
                while (in.available() != 0) {
                    fout.write(in.read());
                    FacesUtils.mostrarMensagem("Upload realizado com sucesso!");
                }
                fout.close();
                if (getNomeArquivoSelecionado() != null) {
                    session = HibernateUtil.getSessionFactory().openSession();
                    tx = session.beginTransaction();
                    session.update(monografias);
                    tx.commit();
                    session.close();
                    setNomeArquivoSelecionado(null);
                    FacesUtils.mostrarMensagem("Arquivo salvo com sucesso!");
                    monografias = new Monografias();
                }
            } catch (Exception ex) {
            }
        }
    }

    public void excluirArquivo() {
        File file = null;
        monografias = (Monografias) (model.getRowData());
        file = new File(dirAbsoluto + monografias.getAutor() + "/" + monografias.getTlptbr());
        if (file.exists()) {
            deletaDir(file);
            file = new File(dirAbsoluto + monografias.getAutor());
            file.delete();
        }
    }

    public static boolean deletaDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                // Deleta os arquivos de dentro do diretório.
                boolean success = deletaDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Agora o diretório está vazio, restando apenas deletá-lo.
        return dir.delete();
    }

    public Monografias getProdutoEditarExcluir() {
        monografias = (Monografias) model.getRowData();
        return monografias;
    }

    public String selectedRowMonografia() {
        monografias = (Monografias) model.getRowData();
        return "/faces/admin/frmExibirDown.xhtml";
    }

    public void prepararAdicionarMonografia(ActionEvent actionEvent) {
        monografias = new Monografias();
    }

    public void abrirArquivo() throws IOException {
        try {
            String dir = "D://projetos/gemono/web/monografias/" + monografias.getAutor() + "/" + monografias.getTlptbr();
            String nomeArq = "";
            File file = new File(dir);
            if (file.exists()) {
                File afile[] = file.listFiles();
                int i = 0;
                for (int j = afile.length; i < j; i++) {
                    File arquivos = afile[i];
                    nomeArq = arquivos.getName();
                }
                File pdfFile = new File(dir + "/" + nomeArq);
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.open(pdfFile);
            } else {
                FacesUtils.mostrarMensagem("Este autor não possui monografia cadastrado em nosso banco de dados!");
            }
        } catch (Exception ex) {
        }
    }

    public List<Monografias> PesquisaMonografias() {
        List<Monografias> list = null;
        if (desc.equals("")) {
            list = procurarTodasMonografias();
        } else {
            list = listaMografiasPorTitulo(desc);
        }
        setExibirForm(true);
        return list;
    }

    public List<Monografias> procurarTodasMonografias() {
        List<Monografias> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Monografias order by autor").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    public List<Monografias> listaMografiasPorTitulo(String titulo) {
        //Cria a sessao com o banco
        session = HibernateUtil.getSessionFactory().openSession();
        //cria a query HQL
        Query q = session.createQuery("from Monografias m where m.tlptbr like :titulo");
        //add o parametro de consulta
        q.setParameter("titulo", "%" + titulo + "%");
        //executa o HQL e o retorno vai para a array
        List<Monografias> listaMonografiasTitulo = q.list();
        return listaMonografiasTitulo;
    }

    public void prepararAlterar(ActionEvent actionEvent) {
        //captura a linha da tabela, fazendo um cast no objeto
        monografias = (Monografias) (model.getRowData());

        System.out.println("O codigo é:" + monografias.getCodigo());

        // atualiza o produto com o produto selecionado e depois atualiza o combo
        selectOneMenuLinhasPesquisas.setValue(monografias.getLinhaspesquisa().getCodigo());
        selectOneMenuLinhasPesquisas.setSubmittedValue(monografias.getLinhaspesquisa().getCodigo());
        // se nao colocar a linha abaixo, após alterar o valor, o evento de value change do combo é disparado!!!!
        selectOneMenuLinhasPesquisas.setLocalValueSet(false);

        // atualiza o produto com o produto selecionado e depois atualiza o combo
        selectOneMenuPrograma.setValue(monografias.getProgramas().getCodigo());
        selectOneMenuPrograma.setSubmittedValue(monografias.getProgramas().getCodigo());
        // se nao colocar a linha abaixo, após alterar o valor, o evento de value change do combo é disparado!!!!
        selectOneMenuPrograma.setLocalValueSet(false);

    }
}
