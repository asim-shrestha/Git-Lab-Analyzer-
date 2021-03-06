import {Avatar, Box, Paper, Typography} from "@material-ui/core";
import formatDate from "../../utils/DateFormatter";
import AnalysisRunStatusIndicator from "./AnalysisRunStatusIndicator";
import AnalysisProgressModal from "../AnalysisProgressModal";
import AppButton from "../app/AppButton";
import {AnalysisRun, AnalysisRunStatus} from "../../interfaces/AnalysisRun";
import React, {useState} from "react";
import {useRouter} from "next/router";

type AnalysisRunListProps = { isLoading: boolean, analysisRuns: AnalysisRun[], loadAnalysisRuns: () => void }
const AnalysisRunList = ({isLoading, analysisRuns, loadAnalysisRuns}: AnalysisRunListProps) => {
    const router = useRouter();
    const [open, setOpen] = useState(false);
    const [selectedAnalysisRun, setSelectedAnalysisRun] = useState<AnalysisRun | null>(null);
    const handleClose = () => {
        setOpen(false);
    };
    const handleViewProgress = (analysisRun: AnalysisRun) =>{
        setSelectedAnalysisRun(analysisRun);
        setOpen(true);
    }

    return (
        <Box>
            {open && <AnalysisProgressModal open={open} handleClose={handleClose} handleWhenProgressIsDone={loadAnalysisRuns} handleError={loadAnalysisRuns} analysisRun={selectedAnalysisRun}/>}
            <Box maxHeight="50vh" overflow="auto">
                {
                    analysisRuns.map((analysis: any) =>
                        <Paper elevation={4} style={{margin: "1em"}} key={analysis.id}>
                            <Box display="flex" alignItems="center" padding={2.5}>
                                <Avatar variant='rounded' style={{width: '4em', height: '4em'}}>
                                    <Typography variant="h3">
                                        {analysis.projectName[0].toUpperCase()}
                                    </Typography>
                                </Avatar>

                                <Box ml={3} flexGrow={1}>
                                    <Typography variant="h5">{analysis.projectNameWithNamespace}</Typography>
                                    <Typography variant="subtitle2">
                                        <b>From:</b> {formatDate(analysis.startDateTime)} - {formatDate(analysis.endDateTime)}
                                    </Typography>
                                    <Typography variant="subtitle2">
                                        <b>Created:</b> {formatDate(analysis.createdDateTime)}
                                    </Typography>
                                    <Typography variant="subtitle2">
                                        <b>Score profile:</b> {analysis.scoreProfileName}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center" flexWrap="wrap">
                                    <AnalysisRunStatusIndicator status={analysis.status}/>
                                    {
                                        analysis.status == AnalysisRunStatus.InProgress &&
                                        <AppButton
                                            color="primary"
                                            onClick={() => handleViewProgress(analysis)}
                                        >
                                            View Progress
                                        </AppButton>
                                    }
                                    <AppButton
                                        color="primary"
                                        onClick={() => router.push(`/project/${analysis.projectId}/0/overview?startDateTime=${analysis.startDateTime}&endDateTime=${analysis.endDateTime}&scoreProfileId=${analysis.scoreProfileId}`)}
                                        disabled={analysis.status != AnalysisRunStatus.Completed}
                                    >
                                        View
                                    </AppButton>
                                </Box>
                            </Box>
                        </Paper>
                    )
                }
            </Box>
            <Box>
                {analysisRuns.length === 0 && !isLoading && <Typography variant="h3" align="center">No analyses found.</Typography>}
                {analysisRuns.length === 0 && isLoading && <Typography variant="h3" align="center">Loading...</Typography>}
            </Box>
            <Box display="flex" justifyContent="center">
                <AppButton
                    color="primary"
                    onClick={loadAnalysisRuns}
                    disabled={isLoading}
                >
                    Sync
                </AppButton>
            </Box>
        </Box>
    );
}

export default AnalysisRunList;