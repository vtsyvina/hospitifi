package com.hospitifi.report;

import com.hospitifi.model.Occupation;
import com.hospitifi.model.Room;
import com.hospitifi.service.RoomService;
import com.hospitifi.util.ServiceFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;
import java.util.List;

public class ReportComposer {
    private static final ReportComposer instance = new ReportComposer();
    private static final String TEMPLATE_PATH = "resources/report_template.docx";
    private static final String OCCUPATION_REPORTS_FOLDER = "reports/occupation/";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/YYYY");

    private static final String ROOM_NUMBER = "t_room_number";
    private static final String CHECK_IN = "t_check_in";
    private static final String CHECK_OUT = "t_check_out";
    private static final String ADULTS = "t_adults";
    private static final String CHILDREN = "t_children";
    private static final String BREAKFAST = "t_breakfast";
    private static final String RATE = "t_rate";
    private static final String ROOM_TOTAL = "t_room_total";
    private static final String BREAKFAST_TOTAL = "t_breakfast_total";
    private static final String ADDITIONAL_TOTAL = "t_additional_total";
    private static final String TOTAL = "t_total";

    private RoomService roomService = ServiceFactory.getRoomService();

    public static ReportComposer getInstance() {
        return instance;
    }

    private ReportComposer() {
    }

    public void createOccupationReport(Occupation occupation, int additionalServices, int breakfastRate) {
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(OPCPackage.open(TEMPLATE_PATH));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                long between = ChronoUnit.DAYS.between(((GregorianCalendar) occupation.getStart()).toZonedDateTime(), ((GregorianCalendar) occupation.getEnd()).toZonedDateTime());
                long breakfastTotal = between * breakfastRate * (occupation.getAdults() + occupation.getChildren());
                long roomTotal = between * occupation.getRate();
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        switch (text) {
                            case ROOM_NUMBER:
                                Room room = roomService.get(occupation.getRoomId());
                                replacePlaceHolder(r, text, ROOM_NUMBER, room.getNumber());
                                break;
                            case CHECK_IN:
                                replacePlaceHolder(r, text, CHECK_IN, DATE_FORMAT.format(occupation.getStart().getTime()));
                                break;
                            case CHECK_OUT:
                                replacePlaceHolder(r, text, CHECK_OUT, DATE_FORMAT.format(occupation.getEnd().getTime()));
                                break;
                            case ADULTS:
                                replacePlaceHolder(r, text, ADULTS, Integer.toString(occupation.getAdults()));
                                break;
                            case CHILDREN:
                                replacePlaceHolder(r, text, CHILDREN, Integer.toString(occupation.getChildren()));
                                break;
                            case BREAKFAST:
                                replacePlaceHolder(r, text, BREAKFAST, (occupation.isBreakfastIncluded() ? "" : "not ") + "included");
                                break;
                            case RATE:
                                replacePlaceHolder(r, text, RATE, Integer.toString(occupation.getRate()));
                                break;
                            case ROOM_TOTAL:
                                replacePlaceHolder(r, text, ROOM_TOTAL, Long.toString(roomTotal));
                                break;
                            case BREAKFAST_TOTAL:
                                replacePlaceHolder(r, text, BREAKFAST_TOTAL, Long.toString(breakfastTotal));
                                break;
                            case ADDITIONAL_TOTAL:
                                replacePlaceHolder(r, text, ADDITIONAL_TOTAL, Integer.toString(additionalServices));
                                break;
                            case TOTAL:
                                replacePlaceHolder(r, text, TOTAL, Long.toString(additionalServices + breakfastTotal + roomTotal));
                                break;
                        }
                    }
                }
            }
        }
//        for (XWPFTable tbl : doc.getTables()) {
//            for (XWPFTableRow row : tbl.getRows()) {
//                for (XWPFTableCell cell : row.getTableCells()) {
//                    for (XWPFParagraph p : cell.getParagraphs()) {
//                        for (XWPFRun r : p.getRuns()) {
//                            String text = r.getText(0);
//                            if (text != null && text.contains("needle")) {
//                                text = text.replace("needle", "haystack");
//                                r.setText(text, 0);
//                            }
//                        }
//                    }
//                }
//            }
//        }
        try {
            int i = 1;
            String reportPath = OCCUPATION_REPORTS_FOLDER + occupation.getId() + "_" + occupation.getGuestName() + ".docx";
            File reportFile = new File(reportPath);
            if (reportFile.exists()) {
                reportPath = OCCUPATION_REPORTS_FOLDER + occupation.getId() + "_" + occupation.getGuestName() + "_" + i + ".docx";
                while (new File(reportPath).exists()) {
                    i++;
                    reportPath = OCCUPATION_REPORTS_FOLDER + occupation.getId() + "_" + occupation.getGuestName() + "_" + i + ".docx";
                }
                reportFile = new File(reportPath);
            }
            if (reportFile.getParentFile() != null) {
                reportFile.getParentFile().mkdirs();
            }
            Files.createFile(reportFile.toPath());
            doc.write(new FileOutputStream(reportPath));
            Desktop.getDesktop().open(reportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replacePlaceHolder(XWPFRun r, String text, String placeHolder, String replacement) {
        text = text.replace(placeHolder, replacement);
        r.setText(text, 0);
    }

}
