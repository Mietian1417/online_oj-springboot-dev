package com.example.demo.service;

import com.example.demo.compile.Task;
import com.example.demo.model.Answer;
import com.example.demo.model.Question;
import com.example.demo.dao.ProblemMapper;
import com.example.demo.model.Problem;
import com.example.demo.param.ProblemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:59
 */

@Service
public class ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private UserService userService;

    public List<Problem> getProblemList() {
        return problemMapper.getProblemList();
    }

    public Problem getProblemById(Integer problemId) {
        return problemMapper.getProblemById(problemId);
    }

    /**
     * 获取到用户代码后, 要拼接测试代码, 再运行整个代码
     *
     * @param userId    用户 id
     * @param problemId 题目 id
     * @param testCode  测试代码
     * @param code      用户提交的代码
     * @return 代码执行结果
     */
    public Answer submitAndSaveCode(int userId, int problemId, String testCode, String code) {

        // 拼接测试代码
        String finalCode = merge(testCode, code);

        if (finalCode == null) {
            Answer answer = new Answer();
            // 前面约定了 1 表示编译出错
            answer.setStatus(1);
            answer.setReason("提交非法代码! ");
            return answer;
        }


        // 执行代码
        Task task = new Task();
        Question question = new Question();
        question.setCode(finalCode);
        Answer answer = task.compileAndRun(question);
        if (answer.getStatus() == 0) {
            // 当用户编译运行没有问题时, 保存(更新)用户当前提交的代码
            userService.saveUserSubmitCode(userId, problemId, code);
        }
        return answer;
    }

    private String merge(String testCode, String submitCode) {
        if (submitCode == null) {
            return null;
        }
        int pos = submitCode.lastIndexOf("}");
        if (pos == -1) {
            return null;
        }

        // 校验代码的合格性
        if (checkMethodIsExist(submitCode)) {
            String str = submitCode.substring(0, pos);
            return str + testCode + "\n}";
        }
        return null;
    }

    private boolean checkMethodIsExist(String submitCode) {
        String test = submitCode.replaceAll(" ", "");
        String test1 = test.replaceAll("\n", "");
        if (test1.charAt(test1.length() - 1) == '}'
                && test1.charAt(test1.length() - 2) == '}') {
            return true;
        }
        return false;
    }

    public int addProblem(ProblemParam problemParam) {
        return problemMapper.addProblem(problemParam);
    }

    public Integer deleteProblemById(Integer problemId) {
        return problemMapper.deleteProblemById(problemId);
    }

}
