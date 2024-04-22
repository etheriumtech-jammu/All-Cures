package controller;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;

import com.google.gson.Gson;

import dao.DoctorsDaoImpl;
import dao.SpecialtiesDaoImpl;
import dao.SubspecialtiesDaoImp;
import model.Specialties;
import model.Subspecialties;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import util.Constant;

/**
 * Servlet implementation class IntegratedActionController
 */
public class IntegratedActionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IntegratedActionController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DoctorsDaoImpl doctors = new DoctorsDaoImpl();
        ArrayList<String> doctorArray = new ArrayList<String>();
        SpecialtiesDaoImpl spl = new SpecialtiesDaoImpl();
        ArrayList<Specialties> splArray = new ArrayList<Specialties>();
        SubspecialtiesDaoImp subspl = new SubspecialtiesDaoImp();
        ArrayList<Subspecialties> subsplArray = new ArrayList<Subspecialties>();

        MemcachedClient mcc = null;
        String address = Constant.ADDRESS;

        try {
            mcc = new MemcachedClient(
                    new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(),
                    AddrUtil.getAddresses(address));
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to connect to Memcached server");
            return;
        }

        Constant.log("Connection to memcache server successful", 1);
        String cachedocnameString = "" + mcc.get(Constant.DOCNAME);
        String cacheSplString = "" + mcc.get(Constant.SPL);
        String cacheSplSubString = "" + mcc.get(Constant.SUBSPL);
        String ct = Constant.NULL;
        if ((cachedocnameString.contains(ct)) && (cacheSplString.contains(ct)) && (cacheSplSubString.contains(ct))) {
            cachedocnameString = Constant.NULL;
            cacheSplString = Constant.NULL;
            cacheSplSubString = Constant.NULL;
            doctorArray = doctors.findAllDoctors();
            splArray = spl.findAllSpecialties();
            subsplArray = subspl.findAllSubSpecialties();
            Constant.log(("Adding to mem cache:" + mcc.add(Constant.DOCNAME, 360000, doctorArray).getStatus()), 1);
            Constant.log(("Adding up in cache:" + mcc.add(Constant.SPL, 360000, splArray).getStatus()), 1);
            Constant.log(("Adding up in cache:" + mcc.add(Constant.SUBSPL, 360000, subsplArray).getStatus()), 1);
        } else {
            doctorArray.addAll((Collection<? extends String>) mcc.get(Constant.DOCNAME));
            splArray.addAll((Collection<? extends Specialties>) mcc.get(Constant.SPL));
            subsplArray.addAll((Collection<? extends Subspecialties>) mcc.get(Constant.SUBSPL));
        }

        JSONObject stringToJsonObject = new JSONObject();
        stringToJsonObject.put(Constant.DOCTORNAME, doctorArray);
        stringToJsonObject.put(Constant.SPECIALTIES, splArray);
        stringToJsonObject.put(Constant.SUBSPECIALTIES, subsplArray);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Encoding", "gzip");

        OutputStream out = response.getOutputStream();

        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(gzipOutputStream, StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            String jsonData = gson.toJson(stringToJsonObject);
            writer.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to compress response");
        } finally {
            out.flush();
//            out.close();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
