import React, {useEffect, useState} from 'react';
import {Commit} from "../../interfaces/Commit";
import DiffItemList, {DiffItem} from "./DiffItemList";

type CommitListProps = {
    commits: Commit[]
    handleSelectCommit: (commit: Commit) => void;
    handleToggle: (commit: Commit) => void;
}

const CommitList = ({commits, handleSelectCommit, handleToggle}: CommitListProps) => {
    const [selectedIndex, setSelectedIndex] = useState(-1);
    useEffect(() => {
        // Reset selection when commits change
        setSelectedIndex(-1);
    }, [commits])

    const handleSelectDiffItem = (diffItem: DiffItem) => {
        const commit = commits.find((commit) => {
            return commit.id == diffItem.id;
        })
        if (!commit) {
            return;
        }
        handleSelectCommit(commit);
    }

    return (
        <DiffItemList
            diffItems={commits}
            diffItemType="Commit"
            handleSelectDiffItem={handleSelectDiffItem}
            selectedIndex={selectedIndex}
            setSelectedIndex={setSelectedIndex}
            handleToggle={handleToggle}
        />
    );
}

export default CommitList;