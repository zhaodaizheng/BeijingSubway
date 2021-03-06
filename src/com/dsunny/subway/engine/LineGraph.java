package com.dsunny.subway.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.dsunny.subway.constant.SubwayConst;

/**
 * @author m 线路抽象图
 * 
 */
public class LineGraph {

    private int linesCount;
    private int totalTimes;
    private int minTimes;
    private int curTimes;
    private Stack<Integer> stack;
    private List<String[]> result;
    private boolean[] isVisited;

    public LineGraph() {
        this.linesCount = SubwayConst.Lines.length;
        this.totalTimes = linesCount;
        this.minTimes = linesCount;
        this.curTimes = 0;
        this.stack = new Stack<Integer>();
        this.result = new ArrayList<String[]>();
        this.isVisited = new boolean[linesCount];
    }

    /**
     * @param startLid
     *            起点线路ID
     * @param endLid
     *            终点线路ID
     * @return 换乘线路
     */
    private void getTransLids(String startLid, String endLid) {
        int startIdx = getLineIndex(startLid);
        int endIdx = getLineIndex(endLid);
        totalTimes = SubwayConst.Graph[startIdx][endIdx];
        minTimes = totalTimes < minTimes ? totalTimes : minTimes;

        if (minTimes == totalTimes) {
            curTimes = 0;
            for (int i = 0; i < linesCount; i++) {
                isVisited[i] = false;
            }

            DFS(startIdx, endIdx);
        }
    }

    /**
     * @param startLid
     *            起点线路ID
     * @param endLid
     *            终点线路ID
     * @return 换乘线路
     */
    public List<String[]> getAllTransLids(String startLid, String endLid) {
        getTransLids(startLid, endLid);
        return result;
    }

    /**
     * @param lstStartLids
     *            起点线路
     * @param lstEndLids
     *            终点线路
     * @return 换乘线路
     */
    public List<String[]> getAllTransLids(List<String> lstStartLids, List<String> lstEndLids) {
        for (String start : lstStartLids) {
            for (String end : lstEndLids) {
                getTransLids(start, end);
            }
        }

        for (int i = result.size(); i > 0; i--) {
            if (result.get(i - 1).length != minTimes + 1) {
                result.remove(i - 1);
            }
        }

        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @return 索引
     */
    private int getLineIndex(String lid) {
        int result = -1;

        int i = 0;
        for (String s : SubwayConst.Lines) {
            if (s.equals(lid)) {
                result = i;
                break;
            }
            i++;
        }

        return result;
    }

    /**
     * @param startIdx
     *            起点线路索引
     * @param endIdx
     *            终点线路索引
     */
    private void DFS(int startIdx, int endIdx) {
        curTimes++;
        if (curTimes <= totalTimes) {
            stack.push(startIdx);
            isVisited[startIdx] = true;
            for (int i = 0; i < linesCount; i++) {
                if (!isVisited[i] && SubwayConst.Graph[startIdx][i] == 1) {
                    DFS(i, endIdx);
                }
            }
            isVisited[startIdx] = false;
            stack.pop();
        } else if (startIdx == endIdx) {
            stack.push(endIdx);
            int i = 0;
            String[] lines = new String[stack.size()];
            for (int line : stack) {
                lines[i++] = SubwayConst.Lines[line];
            }
            result.add(lines);
            stack.pop();
        }
        curTimes--;
    }
}
