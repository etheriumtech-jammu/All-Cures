package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.DoctorsDaoImpl_New;
import model.Doctor_New;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;

import util.Constant;

public class DoctorsActionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static MemcachedClient mcc = null;

    @Override
    public void init() throws ServletException {
        super.init();
        initializeCacheClient();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mcc != null) {
            mcc.shutdown();
        }
    }

    public DoctorsActionController() {
        super();
    }

    public void getProfile(HttpServletRequest request, HttpServletResponse response, boolean jsonResponse) throws ServletException, IOException {
        String id = request.getParameter("DocID");
        String cacheString = null;
        Constant.log("Got Req for Profile For DocID: " + id, 1);
        int DocID = -1;
        DoctorsDaoImpl_New doctorDao = null;
        Doctor_New doctorObj = null;

        if (id != null) {
            DocID = Integer.parseInt(id);
            cacheString = findDocInCache(DocID);
            String jsondata = null;
            if (cacheString == null || "".equals(cacheString) || "null".equalsIgnoreCase(cacheString)) {
                Constant.log("Got Null From MemCache on the Doc:" + id, 1);
                doctorDao = new DoctorsDaoImpl_New();
                doctorObj = doctorDao.getAllDoctorsInfoByDocId(DocID);

                Gson gson = new GsonBuilder().serializeNulls().create();
                jsondata = gson.toJson(doctorObj);
                mcc.add(Constant.DOCID + "_" + id, 360000, jsondata).getStatus();
            } else {
                Constant.log("Found Doctor in Memcache and serving from there", 1);
                jsondata = (String) mcc.get(Constant.DOCID + "_" + id);
                Constant.log("Done Constructing Doctor JSON From Memcache", 1);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Constant.log("Sending Response Now", 0);
            if (jsonResponse) {
                Constant.log("JSON Response Requested", 1);
                PrintWriter out = response.getWriter();
                out.write(jsondata);
                out.flush();
                Constant.log("Responding with JSON data---->" + jsondata, 1);
            } else {
                Constant.log("Responding with Servlet Redirect after putting JsonData in ServletContext---->" + jsondata, 1);
                this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
                response.sendRedirect("/cures/profile.jsp");
            }
        } else {
            if (jsonResponse) {
                response.sendError(404, "No Doctor Found with Id:" + id);
            } else {
                response.sendRedirect(URLEncoder.encode("/cures/error.jsp?msg=Invalid Doctor Id", java.nio.charset.StandardCharsets.UTF_8.toString()));
            }
        }
    }

    public void getProfilebydocid(HttpServletRequest request, HttpServletResponse response, boolean jsonResponse) throws ServletException, IOException {
        String doctorid = request.getParameter("DocID");
        String cacheString = null;
        Constant.log("Got Req for Profile For DocID: " + doctorid, 1);
        int docid = -1;
        DoctorsDaoImpl_New doctorDao = null;
        Doctor_New doctorObj = null;

        if (doctorid != null) {
            docid = Integer.parseInt(doctorid);
            cacheString = findDocInCacheByDocID(docid);
            String jsondata = null;
            if (cacheString == null || "".equals(cacheString) || "null".equalsIgnoreCase(cacheString)) {
                Constant.log("Got Null From MemCache on the Doc:" + doctorid, 1);
                doctorDao = new DoctorsDaoImpl_New();
                doctorObj = doctorDao.getAllDoctorsInfoByDocId(docid);

                Gson gson = new GsonBuilder().serializeNulls().create();
                jsondata = gson.toJson(doctorObj);
                mcc.add(Constant.DOCID + "_" + doctorid, 360000, jsondata).getStatus();
            } else {
                Constant.log("Found Doctor in Memcache and serving from there", 1);
                jsondata = (String) mcc.get(Constant.DOCID + "_" + doctorid);
                Constant.log("Done Constructing Doctor JSON From Memcache", 1);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Constant.log("Sending Response Now", 0);
            if (jsonResponse) {
                Constant.log("JSON Response Requested", 1);
                PrintWriter out = response.getWriter();
                out.write(jsondata);
                out.flush();
                Constant.log("Responding with JSON data---->" + jsondata, 1);
            } else {
                Constant.log("Responding with Servlet Redirect after putting JsonData in ServletContext---->" + jsondata, 1);
                this.getServletContext().setAttribute(Constant.JSONDATA, jsondata);
                response.sendRedirect("/cures/profile.jsp");
            }
        } else {
            if (jsonResponse) {
                response.sendError(404, "No Doctor Found with Id:" + doctorid);
            } else {
                response.sendRedirect(URLEncoder.encode("/cures/error.jsp?msg=Invalid Doctor Id", java.nio.charset.StandardCharsets.UTF_8.toString()));
            }
        }
    }

    public void initializeCacheClient() {
        try {
            Constant.log("Trying Connection to Memcache server", 0);
            mcc = new MemcachedClient(new ConnectionFactoryBuilder().setDaemon(true).setFailureMode(FailureMode.Retry).build(), AddrUtil.getAddresses(Constant.ADDRESS));
            Constant.log("Connection to Memcache server Sucessful", 0);
        } catch (IOException e) {
            e.printStackTrace();
            Constant.log("Connection to Memcache server UN-Sucessful", 3);
        }
    }

    public String findDocInCache(int DocID) {
        String cacheString = null;
        if (mcc == null) {
            initializeCacheClient();
        }
        if (DocID == 945) {
            mcc.delete(Constant.DOCID + "_" + DocID);
        }
        Constant.log("Getting docid from MemCache", 0);
        if (mcc.get(Constant.DOCID + "_" + DocID) != null)
            cacheString = mcc.get(Constant.DOCID + "_" + DocID).toString();
        Constant.log("Found In MemCache:" + cacheString, 0);
        return cacheString;
    }

    public String findDocInCacheByDocID(int doctId) {
        String cacheString = null;
        if (doctId == 74) {
            mcc.delete(Constant.DOCID + "_" + doctId);
        }
        if (mcc == null) {
            initializeCacheClient();
        }
        Constant.log("Getting docid from MemCache", 0);
        if (mcc.get(Constant.DOCID + "_" + doctId) != null)
            cacheString = mcc.get(Constant.DOCID + "_" + doctId).toString();
        Constant.log("Found In MemCache:" + cacheString, 0);
        return cacheString;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        if (cmd != null && "getProfile".equalsIgnoreCase(cmd)) {
            getProfile(request, response, true);
        } else if (cmd != null && "getProfilebydocid".equalsIgnoreCase(cmd)) {
            getProfilebydocid(request, response, true);
        } else {
            getProfile(request, response, false);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
