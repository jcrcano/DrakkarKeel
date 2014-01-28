/*
 * DrakkarKeel - An Enterprise Collaborative Search Platform
 *
 * The contents of this file are subject under the terms described in the
 * DRAKKARKEEL_LICENSE file included in this distribution; you may not use this
 * file except in compliance with the License. 
 *
 * 2013-2014 DrakkarKeel Platform.
 */
package drakkar.stern.tracker.cache;

import drakkar.oar.Seeker;
import drakkar.oar.SessionProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase almacena toda la información relacionada con la sesión
 */
public class SessionProfile {

    private SeekerInfo seekerProxies;
    private SeekerInfo recordWaitAdmission;
    private SeekerSearchResults recordSearches;
    private SeekerRecommendResults recordRecommendation;
    private CommentDocuments commentDocs;
    private ViewedDocuments viewedDocs;
    private SelectedDocuments selectedDocs;
    private Chairman chairman;
    private SessionProperty properties;
    private Count seekerMessagesCount;
    private Count memberRecommendationsCount;
    private Map<String, Long> querys;
    private int searchesCount;
    private int messagesCount;
    private int recommendationsCount;
    private Date startDate;
    private Date stopDate;
    private SeekerRecommends seekerRecommends;
    private Seeker tempChairman; //chairman temporal
    private boolean termsSuggest = false;
    private List<Seeker> activeTermsSuggestList;

    public SessionProfile() {
        this.seekerProxies = new SeekerInfo();
        this.recordWaitAdmission = new SeekerInfo();
        this.properties = new SessionProperty();
        this.recordRecommendation = new SeekerRecommendResults();
        this.commentDocs = new CommentDocuments();
        this.viewedDocs = new ViewedDocuments();
        this.selectedDocs = new SelectedDocuments();
        this.recordSearches = new SeekerSearchResults(properties.getSessionName(), viewedDocs, selectedDocs, commentDocs);
        this.chairman = null;

        this.seekerMessagesCount = new Count();
        this.memberRecommendationsCount = new Count();
        this.querys = new HashMap<>();
        this.searchesCount = 0;
        this.messagesCount = 0;
        this.recommendationsCount = 0;
        this.startDate = new Date();
        this.stopDate = null;
        this.seekerRecommends = new SeekerRecommends();
        this.activeTermsSuggestList = new ArrayList<>();
    }

    public SessionProfile(String sessionName, String sessionDescription) {
        this.seekerProxies = new SeekerInfo();
        this.recordWaitAdmission = new SeekerInfo();
        this.recordRecommendation = new SeekerRecommendResults();
        this.commentDocs = new CommentDocuments();
        this.viewedDocs = new ViewedDocuments();
        this.selectedDocs = new SelectedDocuments();
        this.chairman = null;
        this.properties = new SessionProperty(sessionName, sessionDescription);
        this.recordSearches = new SeekerSearchResults(properties.getSessionName(), viewedDocs, selectedDocs, commentDocs);
        this.seekerMessagesCount = new Count();
        this.memberRecommendationsCount = new Count();
        this.querys = new HashMap<>();
        this.searchesCount = 0;
        this.messagesCount = 0;
        this.recommendationsCount = 0;
        this.startDate = new Date();
        this.stopDate = null;
        this.seekerRecommends = new SeekerRecommends();
        this.activeTermsSuggestList = new ArrayList<>();
    }

    /**
     * Constructor de la clase
     *
     * @param chairman   jefe de la sesión
     * @param members    relación de usuarios con sus objetos proxies
     * @param properties propiedades de la sesión
     */
    public SessionProfile(Chairman chairman, SeekerInfo members, SessionProperty properties) {
        this.seekerProxies = members;
        this.recordWaitAdmission = new SeekerInfo();
        this.recordRecommendation = new SeekerRecommendResults();
        this.commentDocs = new CommentDocuments();
        this.viewedDocs = new ViewedDocuments();
        this.selectedDocs = new SelectedDocuments();
        this.chairman = chairman;
        this.properties = properties;
        this.recordSearches = new SeekerSearchResults(properties.getSessionName(), viewedDocs, selectedDocs, commentDocs);
        this.seekerMessagesCount = new Count();
        this.memberRecommendationsCount = new Count();
        this.querys = new HashMap<>();
        this.searchesCount = 0;
        this.messagesCount = 0;
        this.recommendationsCount = 0;
        this.startDate = new Date();
        this.stopDate = null;
        this.seekerRecommends = new SeekerRecommends();
        this.activeTermsSuggestList = new ArrayList<>();
    }

    /**
     * Devuelve el objeto Chairman
     *
     * @return objeto
     */
    public Chairman getChairman() {
        return chairman;
    }

    /**
     * Modifica el valor del objeto Chairman
     * @param chairman
     */
    public void setChairman(Chairman chairman) {
        this.chairman = chairman;
    }

    /**
     * Devuelve la relación de mensajes efectuados por los miembros de la
     * sesión
     *
     * @return relación de mensajes
     */
    public Count getSeekersMessages() {
        return seekerMessagesCount;
    }

    /**
     * Modifica el valor de la relación de mensajes efectuados por los miembros
     * de la sesión
     *
     * @param seekersMessagesCount nueva relación de mensajes
     */
    public void setSeekersMessages(Count seekersMessagesCount) {
        this.seekerMessagesCount = seekersMessagesCount;
    }

    /**
     * Devuelve la relación de recomendaciones efectuados por los miembros de la
     * sesión
     *
     * @return relación de recomendaciones
     */
    public Count getSeekersRecommendations() {
        return memberRecommendationsCount;
    }

    /**
     * Modifica el valor de la relación de recomendaciones efectuados por los
     * miembros de la sesión
     *
     * @param memberRecommendationsCount
     */
    public void setSeekersRecommendations(Count memberRecommendationsCount) {
        this.memberRecommendationsCount = memberRecommendationsCount;
    }

    /**
     * Devuelve el total de mensajes enviados por los miembros de la sesión
     *
     * @return total de mensajes
     */
    public int getMessagesCount() {
        return messagesCount;
    }

    /**
     * Modifica el valor del total de mensajes enviados por los miembros de la sesión
     *
     * @param messagesCount nuevo valor
     */
    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    /**
     * Devuelve las propiedades de la sesión
     *
     * @return propiedades
     */
    public SessionProperty getProperties() {
        return properties;
    }

    /**
     * Modifica las propiedades de la sesión
     *
     * @param properties nuevas propiedades
     */
    public void setProperties(SessionProperty properties) {
        this.properties = properties;
    }

    /**
     * Devuelve la relación de las consultas efectuadas por los miembros de la
     * sesión con el total de documentos obtenido para cada una de ellas
     *
     * @return relación de consultas
     *
     */
    public Map<String, Long> getQuerys() {
        return querys;
    }

    /**
     * Modifica el valor de la relación de las consultas efectuadas por los miembros
     * de la sesión
     *
     * @param querys nueva relación
     */
    public void setQuerys(Map<String, Long> querys) {
        this.querys = querys;
    }

    /**
     * Devuelve el total de recomendaciones efectuadas por los miembros de la
     * sesión
     *
     * @return total de recomendaciones
     */
    public int getRecommendationsCount() {
        return recommendationsCount;
    }

    /**
     * Modifica el valor del total de recomendaciones efectuadas por los miembros de la
     * sesión
     *
     * @param recommendationsCount nuevo valor
     */
    public void setRecommendationsCount(int recommendationsCount) {
        this.recommendationsCount = recommendationsCount;
    }

    /**
     * Devuelve la instancia del objeto ViewedDocuments
     *
     * @return objeto
     */
    public ViewedDocuments getViewedDocuments() {
        return viewedDocs;
    }

    /**
     * Modifica la instancia del objeto ViewedDocuments
     *
     * @param viewedDocs nueva instancia
     */
    public void setViewedDocuments(ViewedDocuments viewedDocs) {
        this.viewedDocs = viewedDocs;
    }

    /**
     *  Devuelve la instancia del objeto SeekerComments
     *
     * @return objeto
     */
    public CommentDocuments getSeekerComments() {
        return commentDocs;
    }

    /**
     * Modifica la instancia del objeto SeekerComments
     *
     * @param seekerComments nueva instancia
     */
    public void setRecordComments(CommentDocuments seekerComments) {
        this.commentDocs = seekerComments;
    }

    /**
     * Devuelve la instancia del objeto SeekerProxy
     *
     * @return objeto
     */
    public SeekerInfo getSeekerInfo() {
        return seekerProxies;
    }

    /**
     * Modifica la instancia del objeto SeekerProxy
     *
     * @param seekerProxies
     */
    public void setSeekerInfo(SeekerInfo seekerProxies) {
        this.seekerProxies = seekerProxies;
    }

    /**
     * Devuelve la instancia del objeto SeekerSearchResults
     *
     * @return objeto
     */
    public SeekerSearchResults getSeekerSearchResults() {
        return recordSearches;
    }

    /**
     * Modifica la instancia del objeto SeekerSearchResults
     *
     * @param recordSearches nueva instancia
     */
    public void setSeekerSearchResults(SeekerSearchResults recordSearches) {
        this.recordSearches = recordSearches;
    }

    /**
     * Devuelve la instancia del objeto SeekerProxy que representa los usuarios
     * pendientes de admisión a una sesión de búsqueda
     *
     * @return objeto
     */
    public SeekerInfo getSeekerWaitAdmission() {
        return recordWaitAdmission;
    }

    /**
     * Modifica la instancia del objeto SeekerProxy que representa los usuarios
     * pendientes de admisión a una sesión de búsqueda
     *
     * @param recordWaitAdmission nueva instancia
     */
    public void setRecordWaitAdmission(SeekerInfo recordWaitAdmission) {
        this.recordWaitAdmission = recordWaitAdmission;
    }

    /**
     * Devuelve la instancia del objeto SelectedDocuments
     *
     * @return objeto
     */
    public SelectedDocuments getSelectedDocuments() {
        return selectedDocs;
    }

    /**
     * Modifica la instancia del objeto SelectedDocuments
     *
     * @param recordEvaluations nueva instancia
     */
    public void setSelectedDocuments(SelectedDocuments recordEvaluations) {
        this.selectedDocs = recordEvaluations;
    }

    /**
     * Devuelve el total de búsquedas efectuadas por los miembros de la sesión
     *
     * @return total de búsquedas
     */
    public int getSearchesCount() {
        return searchesCount;
    }

    /**
     * Modifica el valor del total de búsquedas efectuadas por los miembros de
     * la sesión
     *
     * @param searchesCount nuevo valor
     */
    public void setSearchesCount(int searchesCount) {
        this.searchesCount = searchesCount;
    }

    /**
     * Devuelve la fecha de inicio de la sesión
     *
     * @return fecha
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Modifica el valor de la fecha de inicio de la sesión
     *
     * @param startDate nueva fecha
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Devuelve la fecha de fin de la sesión
     *
     * @return fecha
     */
    public Date getStopDate() {
        return stopDate;
    }

    /**
     * Modifica la fecha de fin de la sesión
     *
     * @param stopDate nueva fecha
     */
    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    /**
     *
     * @return
     */
    public SeekerRecommendResults getSeekerRecommendResults() {
        return recordRecommendation;
    }

    /**
     *
     * @param recordRecommendation
     */
    public void setSeekerRecommendResults(SeekerRecommendResults recordRecommendation) {
        this.recordRecommendation = recordRecommendation;
    }

    /**
     * @return the seekerRecommend
     */
    public SeekerRecommends getSeekerRecommends() {
        return seekerRecommends;
    }

    /**
     * @param seekerRecommend the seekerRecommend to set
     */
    public void setSeekerRecommends(SeekerRecommends seekerRecommend) {
        this.seekerRecommends = seekerRecommend;
    }

    /**
     *
     * @return
     */
    public Seeker getTempChairman() {
        return tempChairman;
    }

    /**
     *
     * @param tempChairman
     */
    public void setTempChairman(Seeker tempChairman) {
        this.tempChairman = tempChairman;
    }

    public boolean isTermsSuggest() {
        return termsSuggest;
    }

    public void setTermsSuggest(boolean termsSuggest) {
        this.termsSuggest = termsSuggest;
    }

    public List<Seeker> getActiveTermsSuggestList() {
        return activeTermsSuggestList;
    }

    public boolean isActiveTermsSuggest(Seeker seeker){
        return activeTermsSuggestList.contains(seeker);

    }



    

    public void clear() {

        seekerProxies.record.clear();
        recordWaitAdmission.record.clear();
        recordSearches.record.clear();
        recordRecommendation.record.clear();
        commentDocs.record.clear();
        viewedDocs.record.clear();
        selectedDocs.record.clear();
        seekerMessagesCount.record.clear();
        memberRecommendationsCount.record.clear();
        querys.clear();
        searchesCount = 0;
        messagesCount = 0;
        recommendationsCount = 0;
        seekerRecommends.values.clear();

    }
}
