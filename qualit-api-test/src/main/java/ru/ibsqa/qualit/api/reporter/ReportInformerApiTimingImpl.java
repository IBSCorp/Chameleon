package ru.ibsqa.qualit.api.reporter;

import ru.ibsqa.qualit.definitions.repository.api.MetaEndpoint;
import ru.ibsqa.qualit.definitions.repository.api.MetaRequest;
import ru.ibsqa.qualit.i18n.ILocaleManager;
import ru.ibsqa.qualit.reporter.IReportInformer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportInformerApiTimingImpl implements IReportInformer {

    @Autowired
    private ILocaleManager localeManager;

    private List<TimingInfo> infos = new ArrayList<>();
    private Long totalTime = 0L;

    public void fixInfo(MetaEndpoint endpoint, MetaRequest request, Long time) {
        infos.add(TimingInfo.builder().endpoint(endpoint).request(request).time(time).build());
        totalTime += time;
    }

    @Override
    public String renderBefore() {
        infos.clear();
        totalTime = 0L;
        return "";
    }

    @Override
    public String renderAfter() {
        String table = "";
        if (infos.size()>0) {
            String header = "<div class=\"parameters__table_row\">" +
                    "<div class=\"parameters__table_cell parameters__table_cell_name line-ellipsis\" style=\"font-weight: bold\">" +
                    localeManager.getMessage("timingNameReportTitle") + "</div>" +
                    "<div class=\"parameters__table_cell parameters__table_cell_value line-ellipsis\" style=\"font-weight: bold\">" +
                    localeManager.getMessage("timingValueReportTitle") + "</div>" +
                    "</div>" +
                    "</div>";
            String rows = "";
            String rowTemplate = "<div class=\"parameters__table_row\">" +
                    "<div class=\"parameters__table_cell parameters__table_cell_name line-ellipsis\">%s</div>" +
                    "<div class=\"parameters__table_cell parameters__table_cell_value line-ellipsis\">%s</div>" +
                    "</div>";
            for (TimingInfo info : infos) {
                rows = rows + String.format(
                        rowTemplate,
                        info.getEndpoint().getName() + (null != info.getRequest().getName() ? "." + info.getRequest().getName() : ""),
                        String.valueOf(info.getTime()) + "ms");
            }
            String footer = "";
            if (infos.size() > 1) {
                footer += "<div class=\"parameters__table_row\">" +
                        "<div class=\"parameters__table_cell parameters__table_cell_name line-ellipsis\" style=\"font-weight: bold\">" +
                        localeManager.getMessage("totalTimingNameReportTitle") + "</div>" +
                        "<div class=\"parameters__table_cell parameters__table_cell_value line-ellipsis\" style=\"font-weight: bold\">" +
                        String.valueOf(totalTime) + "ms" + "</div>" +
                        "</div>" +
                        "</div>";
            }
            table = "<div class=\"parameters__table\">" +
                    header + rows + footer +
                    "</div>";
        }
        return table;
    }

    @Builder
    private static class TimingInfo {

        @Getter @Setter
        private MetaEndpoint endpoint;

        @Getter @Setter
        private MetaRequest request;

        @Getter @Setter
        private Long time;
    }
}
